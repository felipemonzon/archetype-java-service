package com.moontech.archetype.infrastructure.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moontech.archetype.application.mapper.UserMapper;
import com.moontech.archetype.commons.constant.ApiConstant;
import com.moontech.archetype.commons.constant.ErrorConstant;
import com.moontech.archetype.commons.constant.FormatConstant;
import com.moontech.archetype.infrastructure.exception.custom.ErrorResponse;
import com.moontech.archetype.infrastructure.exception.custom.ForbiddenException;
import com.moontech.archetype.infrastructure.exception.management.ExceptionManagement;
import com.moontech.archetype.infrastructure.model.request.AuthorizationRequest;
import com.moontech.archetype.infrastructure.model.response.LoginResponse;
import com.moontech.archetype.infrastructure.model.response.SecurityResponse;
import com.moontech.archetype.infrastructure.security.constant.SecurityConstants;
import com.moontech.archetype.infrastructure.security.utility.SecurityUtilities;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * User authentication.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
  /** Authentication manager. */
  private final AuthenticationManager authenticationManager;

  /** JWT Key. */
  private final String signingKey;

  /** Access token validity time. */
  private final long validity;

  /** Refresh token validity time */
  private final long refreshTokenValidity;

  /**
   * Class constructor.
   *
   * @param authenticationManager {@code AuthenticationManager}
   * @param signingKey private key of the token
   * @param validity validation time
   * @param antPathRequestMatcher url for authentication
   */
  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      String signingKey,
      long validity,
      RequestMatcher antPathRequestMatcher,
      long refreshTokenValidity) {
    this.authenticationManager = authenticationManager;
    this.signingKey = signingKey;
    this.validity = validity;
    this.refreshTokenValidity = refreshTokenValidity;
    super.setRequiresAuthenticationRequestMatcher(antPathRequestMatcher);
    super.setAuthenticationFailureHandler(new JwtAuthenticationFailureHandler());
  }

  /**
   * Validates the token in authentication.
   *
   * @param request {@code HttpServletRequest}
   * @param response {@code HttpServletResponse}
   * @return {@code AuthenticationManager}
   * @throws AuthenticationException {@code AuthenticationException} exception
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    try {
      AuthorizationRequest userCredentials =
          new ObjectMapper().readValue(request.getInputStream(), AuthorizationRequest.class);

      if (!userCredentials.getUsername().matches(FormatConstant.USERNAME_PATTERN)
          || !userCredentials.getPassword().matches(FormatConstant.PAW_PATTERN)) {
        log.error("Username or password do not meet the characteristics.");
        throw new BadCredentialsException(ErrorConstant.INVALID_CREDENTIAL_USER_MESSAGE);
      }
      return this.authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              userCredentials.getUsername(), userCredentials.getPassword()));
    } catch (IOException ex) {
      log.error(ex.getMessage(), ex);
      throw new BadCredentialsException(ErrorConstant.INVALID_CREDENTIAL_USER_MESSAGE);
    }
  }

  /**
   * Valid authentication, generates the token.
   *
   * @param request {@code HttpServletRequest}
   * @param response {@code HttpServletResponse}
   * @param chain {@code FilterChain}
   * @param authResult {@code Authentication}
   */
  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult)
      throws IOException {

    String accessToken = SecurityUtilities.generateToken(authResult, signingKey, validity);
    String refreshToken =
        SecurityUtilities.generateRefreshToken(authResult, signingKey, refreshTokenValidity);

    LoginResponse login =
        UserMapper.INSTANCE.mapToLoginResponse((SecurityResponse) authResult.getPrincipal());

    response.addHeader(HttpHeaders.AUTHORIZATION, SecurityUtilities.addTokenToHeader(accessToken));
    response.addHeader(SecurityConstants.REFRESH_TOKEN_HEADER_NAME, refreshToken);
    response.getWriter().write(new ObjectMapper().writeValueAsString(login));
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    log.info("Login successfully for user {}", login.getId());
  }

  /**
   * Invalid authentication.
   *
   * @param request {@code HttpServletRequest}
   * @param response {@code HttpServletResponse}
   * @param failed {@code AuthenticationException}
   * @throws IOException {@code IOException} exception
   */
  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
      throws IOException {
    log.error(failed.getMessage(), failed);
    if (failed instanceof BadCredentialsException
        || failed.getCause() instanceof ForbiddenException) {
      response.setContentType(MediaType.APPLICATION_JSON_VALUE);
      response.setCharacterEncoding(StandardCharsets.UTF_8.name());
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response
          .getWriter()
          .print(
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

  /**
   * Resolves Spring placeholder variables.
   *
   * @return {@code PropertySourcesPlaceholderConfigurer}
   */
  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }
}
