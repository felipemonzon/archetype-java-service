package com.moontech.archetype.application.business;

import com.moontech.archetype.application.service.PasswordService;
import com.moontech.archetype.commons.constant.ErrorConstant;
import com.moontech.archetype.commons.constant.FormatConstant;
import com.moontech.archetype.domain.entity.PasswordResetTokenEntity;
import com.moontech.archetype.domain.entity.UserEntity;
import com.moontech.archetype.domain.repository.PasswordResetTokenRepository;
import com.moontech.archetype.domain.repository.UserRepository;
import com.moontech.archetype.infrastructure.exception.custom.BadRequestException;
import com.moontech.archetype.infrastructure.exception.custom.BusinessException;
import com.moontech.archetype.infrastructure.model.request.PasswordResetConfirmRequest;
import com.moontech.archetype.infrastructure.model.request.PasswordResetRequest;
import com.moontech.archetype.infrastructure.model.response.GenericResponse;
import com.moontech.archetype.infrastructure.notification.business.NotificationService;
import com.moontech.archetype.infrastructure.notification.enums.EmailTemplate;
import com.moontech.archetype.infrastructure.notification.enums.NotificationChannel;
import com.moontech.archetype.infrastructure.notification.utilities.NotificationUtilities;
import com.moontech.archetype.infrastructure.security.utility.SecurityUtilities;
import io.micrometer.observation.annotation.Observed;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for password reset operations.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordBusiness implements PasswordService {
  /** User repository for database operations. */
  private final UserRepository userRepository;

  /** Password reset token repository for database operations. */
  private final PasswordResetTokenRepository tokenRepository;

  /** Notification service for sending emails. */
  private final NotificationService notificationService;

  @Value("${api.uri.data.passwordForgot}")
  private String forgotPasswordPath;

  /**
   * Requests a password reset by sending a reset link to the user's email. Returns a generic
   * message even if the email doesn't exist to avoid account enumeration.
   *
   * @param request the password reset request
   * @return a generic response message
   */
  @Override
  @Transactional
  @Observed(name = "request change password")
  public GenericResponse requestPasswordReset(PasswordResetRequest request) {
    Optional.of(
            this.userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                    () ->
                        new BusinessException(
                            ErrorConstant.RECORD_NOT_FOUND_CODE, "User not found")))
        .ifPresent(
            user -> {
              String rawToken = NotificationUtilities.generateToken();
              String hash = NotificationUtilities.sha256Hex(rawToken);

              PasswordResetTokenEntity token = new PasswordResetTokenEntity();
              token.setUser(user);
              token.setTokenHash(hash);
              token.setExpiresAt(Instant.now().plus(1, ChronoUnit.HOURS));
              this.tokenRepository.save(token);

              String baseLink =
                  request.getFrontendUrl() == null || request.getFrontendUrl().isBlank()
                      ? this.forgotPasswordPath
                      : request.getFrontendUrl();

              String link =
                  baseLink + "?token=" + URLEncoder.encode(rawToken, StandardCharsets.UTF_8);

              try {
                this.notificationService.send(
                    NotificationChannel.EMAIL,
                    NotificationUtilities.getSendEmail(user, link, EmailTemplate.FORGOT_PASSWORD));
              } catch (Exception e) {
                log.warn("Failed to send reset email to {}: {}", user.getEmail(), e.getMessage());
              }
            });
    return GenericResponse.builder()
        .message("If the email exists, a reset link has been sent.")
        .build();
  }

  /**
   * Confirms password reset by validating the token and updating the user's password.
   *
   * @param request the password reset confirmation request
   * @return a generic response message
   * @throws BadRequestException if the token is invalid, expired, or the password format is invalid
   */
  @Override
  @Transactional
  @Observed(name = "request confirm password reset")
  public GenericResponse confirmPasswordReset(PasswordResetConfirmRequest request) {
    String hash = NotificationUtilities.sha256Hex(request.getToken());
    PasswordResetTokenEntity token =
        this.tokenRepository
            .findByTokenHash(hash)
            .orElseThrow(() -> new BadRequestException("Invalid token", Collections.emptyList()));

    if (Boolean.TRUE.equals(token.getUsed()) || token.getExpiresAt().isBefore(Instant.now())) {
      throw new BadRequestException("Invalid or expired token", Collections.emptyList());
    }

    UserEntity user = token.getUser();

    // basic password format validation
    if (request.getPassword() == null
        || !request.getPassword().matches(FormatConstant.PAW_PATTERN)) {
      throw new BadRequestException("Invalid password format", Collections.emptyList());
    }

    user.setPassword(SecurityUtilities.passwordEncoder(request.getPassword()));
    this.userRepository.save(user);

    token.setUsed(Boolean.TRUE);
    this.tokenRepository.save(token);

    try {
      this.notificationService.send(
          NotificationChannel.EMAIL,
          NotificationUtilities.getSendEmail(
              user, StringUtils.EMPTY, EmailTemplate.RESET_PASSWORD, request));
    } catch (Exception e) {
      log.warn("Failed to send confirmation email to {}: {}", user.getEmail(), e.getMessage());
    }
    return GenericResponse.builder().message("Password has been reset successfully.").build();
  }
}
