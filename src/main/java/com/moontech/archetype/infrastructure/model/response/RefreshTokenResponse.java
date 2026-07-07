package com.moontech.archetype.infrastructure.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response for refresh token endpoint.
 *
 * @author Felipe Monzón
 * @since 2024-07-29
 */
@Getter
@Setter
@Builder
public class RefreshTokenResponse {
  /** Properties for user. */
  private LoginResponse userData;

  /** New access token. */
  private String accessToken;

  /** New refresh token. */
  private String refreshToken;
}
