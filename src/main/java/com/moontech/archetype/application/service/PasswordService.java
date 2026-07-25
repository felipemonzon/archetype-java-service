package com.moontech.archetype.application.service;

import com.moontech.archetype.infrastructure.model.request.PasswordResetConfirmRequest;
import com.moontech.archetype.infrastructure.model.request.PasswordResetRequest;
import com.moontech.archetype.infrastructure.model.response.GenericResponse;

/**
 * Service interface for handling password reset operations.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
public interface PasswordService {
  /**
   * Request a password reset for the user identified in the request.
   *
   * @param request contains the user's email and optional frontend URL to build the reset link
   * @return a generic response indicating success
   */
  GenericResponse requestPasswordReset(PasswordResetRequest request);

  /**
   * Confirms a password reset using the provided token and sets the new password.
   *
   * @param request contains the reset token and the new password
   * @return a generic response indicating the outcome
   */
  GenericResponse confirmPasswordReset(PasswordResetConfirmRequest request);
}
