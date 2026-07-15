package com.moontech.archetype.controller;

import com.moontech.archetype.application.business.SecurityBusiness;
import com.moontech.archetype.configuration.BaseTestConfiguration;
import com.moontech.archetype.constants.TestConstants;
import com.moontech.archetype.infrastructure.exception.custom.ForbiddenException;
import com.moontech.archetype.infrastructure.model.response.RefreshTokenResponse;
import com.moontech.archetype.infrastructure.security.constant.SecurityConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Unit tests for {@link com.moontech.archetype.infrastructure.controller.AuthController}.
 *
 * @author Felipe Monzón
 * @since 2024-07-13
 */
@Slf4j
@Sql(
    scripts = {"/db/login-script-test.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class AuthControllerTests extends BaseTestConfiguration {
  /** Path for refresh token. */
  private static final String REFRESH_TOKEN_PATH = "/auth/refresh-token";

  /** Security business logic. */
  @MockitoBean private SecurityBusiness securityBusiness;

  /**
   * Test for successful refresh token.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Successful refresh token")
  void refreshTokenSuccess(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    var response =
        RefreshTokenResponse.builder()
            .refreshToken(TestConstants.TEST_TOKEN)
            .accessToken(TestConstants.TEST_TOKEN)
            .userData(TestConstants.getLoginResponse())
            .build();

    Mockito.when(this.securityBusiness.refreshToken(Mockito.anyString())).thenReturn(response);

    this.mockMvc
        .perform(MockMvcRequestBuilders.post(REFRESH_TOKEN_PATH).headers(this.getHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(TestConstants.TEST_ID))
        .andExpect(MockMvcResultMatchers.jsonPath("$.username").value(TestConstants.USERNAME));
  }

  /**
   * Test for invalid refresh token (secondary flow).
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Invalid refresh token")
  void refreshTokenInvalid(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    Mockito.when(this.securityBusiness.refreshToken(Mockito.anyString()))
        .thenThrow(new ForbiddenException(0, "Invalid refresh token"));

    this.mockMvc
        .perform(MockMvcRequestBuilders.post(REFRESH_TOKEN_PATH).headers(this.getHttpHeaders()))
        .andExpect(MockMvcResultMatchers.status().isForbidden())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid refresh token"));
  }

  /**
   * Test for missing refresh token in request (secondary flow).
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Missing refresh token")
  void refreshTokenMissing(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(REFRESH_TOKEN_PATH)
                .header(
                    HttpHeaders.AUTHORIZATION,
                    SecurityConstants.TOKEN_BEARER_PREFIX
                        + StringUtils.SPACE
                        + this.generateTestToken()))
        .andExpect(MockMvcResultMatchers.status().isInternalServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(9009))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.moreInfo")
                .value(
                    "Required request header 'refresh_token' for method parameter type String is not present"));
  }
}
