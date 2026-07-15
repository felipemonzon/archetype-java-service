package com.moontech.archetype.infrastructure.security.property;

import java.util.List;
import lombok.*;

/**
 * Externalized security properties.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Getter
@Builder
public class SecurityProperties {
  /** JWT key. */
  private final String jwtKey;

  /** Token lifetime. */
  private final long jwtLifeTime;

  /** Refresh token lifetime. */
  private final long refreshTokenLifeTime;

  /** Path for user authentication. */
  private final String userAuthenticationPath;

  /** Path for user confirmation. */
  private String userConfirmTokenPath;

  /** Path for forgotten password. */
  private String forgotPasswordPath;

  /** List of allowed domains. */
  private List<String> cors;
}
