package com.moontech.archetype.infrastructure.controller;

import com.moontech.archetype.application.service.PasswordService;
import com.moontech.archetype.commons.utilities.Utilities;
import com.moontech.archetype.infrastructure.config.GeoLocationConfig;
import com.moontech.archetype.infrastructure.model.request.PasswordResetConfirmRequest;
import com.moontech.archetype.infrastructure.model.request.PasswordResetRequest;
import com.moontech.archetype.infrastructure.model.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for password reset operations.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.uri.domain.users}")
public class PasswordController {
  /** Service for password reset operations. */
  private final PasswordService passwordResetService;

  private final GeoLocationConfig geoLocationConfig;

  /**
   * Request password reset by sending a reset link to the user's email.
   *
   * @param request the password reset request containing user email
   * @return response entity with a success message
   */
  @PostMapping("${api.uri.data.reset}")
  public ResponseEntity<GenericResponse> requestReset(
      @Valid @RequestBody PasswordResetRequest request) {
    return ResponseEntity.ok().body(this.passwordResetService.requestPasswordReset(request));
  }

  /**
   * Confirm password reset by validating the token and updating the password.
   *
   * @param request the password reset confirmation request containing the token and new password
   */
  @PostMapping("${api.uri.data.reset}/confirm")
  public ResponseEntity<GenericResponse> confirmReset(
      @Valid @RequestBody PasswordResetConfirmRequest request, HttpServletRequest httpRequest) {
    request.setIp(Utilities.getClientIp(httpRequest));
    request.setDevice(Utilities.getDeviceInfo(httpRequest));
    request.setCurrentDate(Utilities.getCurrentDateTime());
    request.setIpLocation(Utilities.getGeoLocation(request.getIp(), geoLocationConfig));
    return ResponseEntity.ok().body(this.passwordResetService.confirmPasswordReset(request));
  }
}
