package com.moontech.archetype.infrastructure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moontech.archetype.application.business.SecurityBusiness;
import com.moontech.archetype.commons.constant.ApiConstant;
import com.moontech.archetype.commons.constant.ErrorConstant;
import com.moontech.archetype.infrastructure.exception.custom.ErrorResponse;
import com.moontech.archetype.infrastructure.exception.management.ExceptionManagement;
import com.moontech.archetype.infrastructure.security.constant.SecurityConstants;
import com.moontech.archetype.infrastructure.security.property.SecurityProperties;
import com.moontech.archetype.infrastructure.security.utility.SecurityUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Service authorization filter.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
public class JwtAuthorizationFilter extends OncePerRequestFilter {
  /** Security properties {@code SecurityProperties} . */
  private final SecurityProperties securityProperties;

  /** User service. */
  private final SecurityBusiness loginBusiness;

  public JwtAuthorizationFilter(
      SecurityProperties securityProperties, SecurityBusiness loginBusiness) {
    this.loginBusiness = loginBusiness;
    this.securityProperties = securityProperties;
  }

  /**
   * Filter that validates if the "Authorization" header is present and if the provided token is
   * active and valid.
   *
   * @param httpServletRequest {@code HttpServletRequest}
   * @param httpServletResponse {@code HttpServletResponse}
   * @param filterChain {@code FilterChain}
   * @throws ServletException {@code ServletException} exception
   * @throws IOException {@code IOException} exception
   */
  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse,
      FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationHeader =
        Objects.isNull(httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION))
            ? StringUtils.EMPTY
            : httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

    if (!authorizationHeader.startsWith(SecurityConstants.TOKEN_BEARER_PREFIX)) {
      logger.debug("Invalid prefix token");
      httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
      httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
      if (!httpServletRequest.getRequestURI().contains("/swagger")
          && !httpServletRequest.getRequestURI().contains("/v3/api-docs")
          && !httpServletRequest.getRequestURI().contains("/favicon.ico")
          && !httpServletRequest.getRequestURI().contains("/users/password/reset")
          && !httpServletRequest.getRequestURI().contains("/actuator")
          && !httpServletRequest.getRequestURI().contains("/health")) {
        httpServletResponse
            .getWriter()
            .print(
                new ObjectMapper()
                    .writeValueAsString(
                        ErrorResponse.builder()
                            .type(ExceptionManagement.ErrorType.INVALID.name())
                            .code(ErrorConstant.INVALID_TOKEN_CODE)
                            .message(ErrorConstant.INVALID_TOKEN_MESSAGE)
                            .uuid(httpServletRequest.getHeader(ApiConstant.HEADER_UUID))
                            .build()));
      }
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

    final String token = SecurityUtilities.getTokenByHeader(authorizationHeader);

    String userName = SecurityUtilities.getUserName(token, this.securityProperties.getJwtKey());
    UserDetails user = this.loginBusiness.loadUserByUsername(userName);

    UsernamePasswordAuthenticationToken authenticationToken =
        SecurityUtilities.getAuthentication(token, user, this.securityProperties.getJwtKey());
    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}