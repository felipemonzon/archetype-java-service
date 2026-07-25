package com.moontech.archetype.constants;

import com.moontech.archetype.infrastructure.model.request.AuthorizationRequest;
import com.moontech.archetype.infrastructure.model.response.LoginResponse;
import lombok.experimental.UtilityClass;

/**
 * Constants for tests.
 *
 * @author Felipe Monzón
 * @since 2026-07-13
 */
@UtilityClass
public class TestConstants {
  /** UUID header. */
  public static final String UUID_HEADER = "uuid";

  /** X-Forwarded-For header. */
  public static final String X_FORWARDED_HEADER = "X-Forwarded-For";

  /** Admin profile. */
  public static final String ROLE_ADMIN = "ADMIN";

  /** Other profile. */
  public static final String ROLE_OTHER = "OTHER";

  /** Log running. */
  public static final String TEST_RUNNING = "Running {}";

  /** Test username. */
  public static final String USERNAME = "test_user";

  /** Test id. */
  public static final String TEST_ID = "USU324htgd243yt567jh";

  /** Test token. */
  public static final String TEST_TOKEN =
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0X3VzZXIiLCJDTEFJTV9UT0tFTiI6IkZBQ1RPUl9QQVNTV09SRCIsImlhdCI6MTc4NTIwNDkwMywiaXNzIjoiSVNTVUVSIiwiZXhwIjoyNDE2MzQyOTAzfQ.4OPaimofLj39g7jCqQnfNE7XTfDUUa3cxpoKeufXMTUuflpAB7ofXmkr_IPLZQumsTWwI4zvqpdswOsqwOjQtg";

  /** Test for bad username. */
  public static final String BAD_USERNAME = "test_user_21";

  /** X-Forwarded-For header value. */
  public static final String X_FORWARDER_HEADER_VALUE = "Mozilla/5.0 (Linux; Android 10)";

  /** Test IP. */
  public static final String TEST_IP = "192.168.1.1";

  /** Test token password. */
  public static final String TEST_TOKEN_PASSWORD = "ojKvAxV5zJOMp6cHJIac0-D0X9uykaQWx5NJ5G6ONWE";

  /**
   * LoginResponse.
   *
   * @return loginResponse
   */
  public LoginResponse getLoginResponse() {
    return LoginResponse.builder()
        .id(TestConstants.TEST_ID)
        .username(TestConstants.USERNAME)
        .build();
  }

  /**
   * Creates an authorization request.
   *
   * @param username username property
   * @param password password property
   * @return {@link AuthorizationRequest}
   */
  public AuthorizationRequest getAuthorizationRequest(String username, String password) {
    AuthorizationRequest response = new AuthorizationRequest();
    response.setUsername(username);
    response.setPassword(password);
    return response;
  }
}
