package com.moontech.archetype.constants;

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

  /** Admin profile. */
  public static final String ROLE_ADMIN = "ADMIN";

  /** Other profile. */
  public static final String ROLE_OTHER = "OTHER";

  /** Log running. */
  public static final String TEST_RUNNING = "Running {}";

  /** Test username. */
  public static final String USERNAME = "felipemonzon2705";

  /** Test id. */
  public static final String TEST_ID = "USU324htgd243yt567jh";

  /** Test token. */
  public static final String TEST_TOKEN =
      "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmZWxpcGVtb256b24yNzA1IiwiQ0xBSU1fVE9LRU4iOiJST0xFX0FETUlOLEZBQ1RPUl9QQVNTV09SRCIsImlhdCI6MTc4MzM4ODMyMCwiaXNzIjoiSVNTVUVSIiwiZXhwIjoyNDE0NTI2MzIwfQ.QruWau3Dg9cMjsiy8hvYPeWSQCOABVyUt76V4xm0IAqCZhqpzXaHPKR2n0MRQ74dbJmSTIQJxqdmY-ybGqGbmQ";

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
}
