package com.moontech.archetype.infrastructure.controller;

import com.moontech.archetype.application.business.SecurityBusiness;
import com.moontech.archetype.infrastructure.model.response.LoginResponse;
import com.moontech.archetype.infrastructure.model.response.RefreshTokenResponse;
import com.moontech.archetype.infrastructure.security.constant.SecurityConstants;
import com.moontech.archetype.infrastructure.security.utility.SecurityUtilities;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentication operations.
 *
 * @author Felipe Monzón
 * @since 2024-07-29
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  /** Security business logic. */
  private final SecurityBusiness securityBusiness;

  /**
   * Endpoint to refresh an access token using a refresh token.
   *
   * @param refreshToken containing the refresh token
   * @return {@link ResponseEntity} with {@link LoginResponse} and new access token in header
   */
  @PostMapping("/refresh-token")
  public ResponseEntity<LoginResponse> refreshToken(
      @RequestHeader(SecurityConstants.REFRESH_TOKEN_HEADER_NAME) @Valid @NotEmpty
          String refreshToken) {
    RefreshTokenResponse refreshTokenResponse = this.securityBusiness.refreshToken(refreshToken);
    HttpHeaders headers = new HttpHeaders();
    headers.add(
        HttpHeaders.AUTHORIZATION,
        SecurityUtilities.addTokenToHeader(refreshTokenResponse.getAccessToken()));
    headers.add(SecurityConstants.REFRESH_TOKEN_HEADER_NAME, refreshTokenResponse.getRefreshToken());
    return ResponseEntity.ok().headers(headers).body(refreshTokenResponse.getUserData());
  }
}
