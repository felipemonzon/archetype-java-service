package com.moontech.archetype.infrastructure.notification.utilities;

import com.moontech.archetype.domain.entity.UserEntity;
import com.moontech.archetype.infrastructure.model.request.PasswordResetConfirmRequest;
import com.moontech.archetype.infrastructure.notification.constants.NotificationConstants;
import com.moontech.archetype.infrastructure.notification.enums.EmailTemplate;
import com.moontech.archetype.infrastructure.notification.model.NotificationDTO;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Utility methods for building notification payloads and generating secure tokens.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@UtilityClass
public class NotificationUtilities {
  private static final SecureRandom SECURE_RANDOM = new SecureRandom();

  /**
   * Builds a notification DTO for the password reset email using the given user and URL.
   *
   * @param userData recipient user data
   * @param path the reset link URL to include in the email
   * @return a populated NotificationDTO ready to be sent
   */
  public static NotificationDTO getSendEmail(
      UserEntity userData, String path, EmailTemplate template) {
    Map<String, String> model = new HashMap<>();
    model.put(NotificationConstants.MAIL_FULL_NAME_PROPERTY, joinFullName(userData));
    model.put(NotificationConstants.MAIL_URL_PROPERTY, getUrlValidationEmail(path));

    return NotificationDTO.builder()
        .to(userData.getEmail())
        .templateName(template)
        .model(model)
        .build();
  }

  public static NotificationDTO getSendEmail(
      UserEntity userData,
      String path,
      EmailTemplate template,
      PasswordResetConfirmRequest request) {
    Map<String, String> model = new HashMap<>();
    model.put(NotificationConstants.MAIL_FULL_NAME_PROPERTY, joinFullName(userData));
    model.put(NotificationConstants.MAIL_URL_PROPERTY, getUrlValidationEmail(path));
    model.put(NotificationConstants.MAIL_EMAIL_PROPERTY, userData.getEmail());
    model.put(NotificationConstants.MAIL_IP_PROPERTY, request.getIp());
    model.put(NotificationConstants.MAIL_IP_LOCATION_PROPERTY, request.getIpLocation());
    model.put(NotificationConstants.MAIL_DEVICE_PROPERTY, request.getDevice());
    model.put(NotificationConstants.MAIL_CURRENT_DATE_PROPERTY, request.getCurrentDate());
    model.put(NotificationConstants.MAIL_RESET_URL_PROPERTY, getUrlValidationEmail(path));

    return NotificationDTO.builder()
        .to(userData.getEmail())
        .templateName(template)
        .model(model)
        .build();
  }

  /**
   * Generate url to frontend or app to redirect another page.
   *
   * @param path path with token uri
   * @return complete path to redirect
   */
  private static String getUrlValidationEmail(final String path) {
    return ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + path;
  }

  /**
   * Generates a secure random URL-safe token (Base64 URL without padding).
   *
   * @return generated token string
   */
  public String generateToken() {
    byte[] b = new byte[32];
    SECURE_RANDOM.nextBytes(b);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(b);
  }

  /**
   * Returns the SHA-256 hex representation of the provided input string.
   *
   * @param input the input to hash
   * @return hexadecimal SHA-256 digest
   */
  public String sha256Hex(String input) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] d = md.digest(input.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder();
      for (byte by : d) {
        sb.append(String.format("%02x", by));
      }
      return sb.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Join a full name.
   *
   * @param user user data
   * @return full name
   */
  public String joinFullName(UserEntity user) {
    return Optional.ofNullable(user)
        .map(
            us ->
                Stream.of(us.getFirstName(), us.getLastName())
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.trim().isEmpty())
                    .collect(Collectors.joining(StringUtils.SPACE)))
        .orElse(StringUtils.EMPTY);
  }
}
