package com.moontech.archetype.infrastructure.security.config;

import com.moontech.archetype.infrastructure.security.property.SecurityProperties;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Externalized security properties.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Configuration
public class SecurityPropertyConfiguration {
  /** JWT key. */
  @Value("${security.jwt.key}")
  private String signingKey;

  /** Token lifetime. */
  @Value("${security.jwt.lifeTime}")
  private long validity;

  /** Refresh token lifetime. */
  @Value("${security.jwt.refreshLifeTime}")
  private long refreshValidity;

  /** Path for user authentication. */
  @Value("${api.uri.data.authentication}")
  private String userAuthenticationPath;

  /** Path for user confirmation. */
  @Value("${api.uri.data.confirm}")
  private String userConfirmTokenPath;

  /** Path to reset user password. */
  @Value("${api.uri.data.passwordForgot}")
  private String passwordForgotPath;

  /** List of allowed domains. */
  @Value("${security.cors.origins}")
  private List<String> cors;

  /**
   * Loads externalized variables into the model.
   *
   * @return {@code SecurityProperties}
   */
  @Bean
  public SecurityProperties loadSecurityProperties() {
    return SecurityProperties.builder()
        .userAuthenticationPath(this.userAuthenticationPath)
        .jwtKey(this.signingKey)
        .jwtLifeTime(this.validity)
        .refreshTokenLifeTime(this.refreshValidity)
        .userConfirmTokenPath(this.userConfirmTokenPath)
        .forgotPasswordPath(this.passwordForgotPath)
        .cors(this.cors)
        .build();
  }
}