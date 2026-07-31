package com.moontech.archetype.infrastructure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moontech.archetype.commons.constant.ApiConstant;
import com.moontech.archetype.commons.constant.ErrorConstant;
import com.moontech.archetype.infrastructure.exception.custom.ErrorResponse;
import com.moontech.archetype.infrastructure.exception.management.ExceptionManagement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Handler for authentication failures that returns a standardized JSON error response.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private final HttpStatus statusErrorResponse;

  /**
   * Creates a handler with a custom HTTP status for error responses.
   *
   * @param statusErrorResponse the HTTP status to return on authentication failure
   */
  public JwtAuthenticationFailureHandler(HttpStatus statusErrorResponse) {
    this.statusErrorResponse = statusErrorResponse;
  }

  /** Creates a handler with the default HTTP UNAUTHORIZED status. */
  public JwtAuthenticationFailureHandler() {
    this.statusErrorResponse = HttpStatus.UNAUTHORIZED;
  }

  /**
   * Handles authentication failure and writes a JSON error response.
   *
   * @param request the HTTP servlet request
   * @param response the HTTP servlet response where the error will be written
   * @param e the authentication exception that triggered the failure
   * @throws IOException if writing the response fails
   */
  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
      throws IOException {
    response.setStatus(this.statusErrorResponse.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response
        .getWriter()
        .append(
            new ObjectMapper()
                .writeValueAsString(
                    ErrorResponse.builder()
                        .type(ExceptionManagement.ErrorType.INVALID.name())
                        .code(ErrorConstant.INVALID_CREDENTIAL_USER_CODE)
                        .message(ErrorConstant.INVALID_CREDENTIAL_USER_MESSAGE)
                        .uuid(request.getHeader(ApiConstant.HEADER_UUID))
                        .build()));
  }
}
