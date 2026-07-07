package com.moontech.archetype.application.business;

import com.moontech.archetype.application.mapper.UserMapper;
import com.moontech.archetype.commons.enums.Status;
import com.moontech.archetype.domain.entity.UserEntity;
import com.moontech.archetype.domain.repository.UserRepository;
import com.moontech.archetype.infrastructure.exception.custom.ForbiddenException;
import com.moontech.archetype.infrastructure.model.response.LoginResponse;
import com.moontech.archetype.infrastructure.model.response.RefreshTokenResponse;
import com.moontech.archetype.infrastructure.model.response.SecurityResponse;
import com.moontech.archetype.infrastructure.security.property.SecurityProperties;
import com.moontech.archetype.infrastructure.security.utility.SecurityUtilities;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Login business logic.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityBusiness implements UserDetailsService {
  /** User repository. */
  private final UserRepository userRepository;

  /** Security properties. */
  private final SecurityProperties securityProperties;

  @Override
  @Transactional
  @Observed(name = "login", contextualName = "login")
  public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
    log.info("Load user by username process started...");
    UserEntity user =
        userRepository
            .findByUsernameAndStatus(username, Status.ACTIVE)
            .orElseThrow(
                () -> new UsernameNotFoundException("User Not Found with username: " + username));
    return UserMapper.INSTANCE.mapToSecurityResponse(user);
  }

  /**
   * Refreshes the access token using a valid refresh token.
   *
   * @param refreshToken the refresh token
   * @return a new {@link RefreshTokenResponse} with updated access and refresh tokens
   */
  @Transactional
  @Observed(name = "refreshToken", contextualName = "refreshToken")
  public RefreshTokenResponse refreshToken(String refreshToken) {
    String username =
        SecurityUtilities.getUserName(refreshToken, this.securityProperties.getJwtKey());
    UserDetails userDetails = loadUserByUsername(username);

    if (!SecurityUtilities.validateToken(
        refreshToken, userDetails, this.securityProperties.getJwtKey())) {
      throw new ForbiddenException(0, "Invalid refresh token");
    }

    Authentication authentication =
        SecurityUtilities.getAuthentication(
            refreshToken, userDetails, this.securityProperties.getJwtKey());

    String newAccessToken =
        SecurityUtilities.generateToken(
            authentication,
            this.securityProperties.getJwtKey(),
            this.securityProperties.getJwtLifeTime());

    String newRefreshToken =
        SecurityUtilities.generateRefreshToken(
            authentication,
            this.securityProperties.getJwtKey(),
            this.securityProperties.getRefreshTokenLifeTime());

    LoginResponse loginResponse =
        UserMapper.INSTANCE.mapToLoginResponse((SecurityResponse) userDetails);

    return RefreshTokenResponse.builder()
        .userData(loginResponse)
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build();
  }
}
