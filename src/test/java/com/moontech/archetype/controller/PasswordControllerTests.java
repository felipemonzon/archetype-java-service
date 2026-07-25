package com.moontech.archetype.controller;

import com.moontech.archetype.configuration.BaseTestConfiguration;
import com.moontech.archetype.constants.TestConstants;
import com.moontech.archetype.infrastructure.model.request.PasswordResetConfirmRequest;
import com.moontech.archetype.infrastructure.model.request.PasswordResetRequest;
import com.moontech.archetype.infrastructure.model.response.GenericResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Unit tests for {@link com.moontech.archetype.infrastructure.controller.PasswordController}.
 *
 * <p>Tests cover password reset request and confirmation flows, including success scenarios and
 * error handling. Follows project testing patterns with MockMvc for HTTP testing and Mockito for
 * mocking service dependencies.
 *
 * @author Felipe Monzón
 * @since 2026-07-26
 */
@Slf4j
@Sql(
    scripts = "/db/password-reset-script-test.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class PasswordControllerTests extends BaseTestConfiguration {
  /** Base path for password endpoints. */
  private static final String PASSWORD_BASE_PATH = "/users/password/reset";

  /**
   * Test a successful password reset request.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Request password reset successfully")
  void requestPasswordResetSuccess(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail("test@enterprise.com");
    request.setFrontendUrl("https://app.example.com/reset");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message")
                .value("If the email exists, a reset link has been sent."));
  }

  /**
   * Test a password reset request with the invalid email format.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Request password reset with invalid email")
  void requestPasswordResetInvalidEmail(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail("invalid-email");
    request.setFrontendUrl("https://app.example.com/reset");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                .header(TestConstants.UUID_HEADER, String.valueOf(UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Test password reset request with missing email.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Request password reset with missing email")
  void requestPasswordResetMissingEmail(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail(null);
    request.setFrontendUrl("https://app.example.com/reset");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                .header(TestConstants.UUID_HEADER, String.valueOf(UUID.randomUUID()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Test successful password reset confirmation.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Confirm password reset successfully")
  void confirmPasswordResetSuccess(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
    request.setToken(TestConstants.TEST_TOKEN_PASSWORD);
    request.setPassword("1234567");

    var result =
        this.mockMvc
            .perform(
                MockMvcRequestBuilders.post(PASSWORD_BASE_PATH + "/confirm")
                    .headers(this.getHttpHeaders())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.objectMapper.writeValueAsString(request)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn();

    var genericResponse =
        this.objectMapper.readValue(
            result.getResponse().getContentAsString(), GenericResponse.class);

    Assertions.assertEquals("Password has been reset successfully.", genericResponse.getMessage());
  }

  /**
   * Test password reset confirmation with the invalid token.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Confirm password reset with invalid token")
  void confirmPasswordResetInvalidToken(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
    request.setToken("invalid-token");
    request.setPassword("NewSecurePassword123!");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH + "/confirm")
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid token"));
  }

  /**
   * Test password reset confirmation with the missing token.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Confirm password reset with missing token")
  void confirmPasswordResetMissingToken(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
    request.setToken(null);
    request.setPassword("NewSecurePassword123!");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH + "/confirm")
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Test password reset confirmation with the missing password.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Confirm password reset with missing password")
  void confirmPasswordResetMissingPassword(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
    request.setToken("valid-token");
    request.setPassword(null);

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH + "/confirm")
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Test password reset confirmation with the weak password.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Confirm password reset with invalid password format")
  void confirmPasswordResetWeakPassword(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
    request.setToken("rAwPjIIuAcZCLFVSLvTc0DApaOG47_a1ufbeqoxwhNk");
    request.setPassword("weak!");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH + "/confirm")
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid password format"));
  }

  /**
   * Test password reset request with the empty email string.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Request password reset with empty email")
  void requestPasswordResetEmptyEmail(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail("");
    request.setFrontendUrl("https://app.example.com/reset");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  /**
   * Test confirm password reset with client IP extraction.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Confirm password reset extracts client IP")
  void confirmPasswordResetExtractsClientIp(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetConfirmRequest request = new PasswordResetConfirmRequest();
    request.setToken("ysvLh8gC697ZpyBlPjPf7KELtaaZhU3MzrRnGMln27Q");
    request.setPassword("SecurePassword123");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH + "/confirm")
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message")
                .value("Password has been reset successfully."));
  }

  /**
   * Test password reset request with the optional frontend URL.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Request password reset with optional frontend URL")
  void requestPasswordResetWithFrontendUrl(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail("test@enterprise.com");
    request.setFrontendUrl("https://custom-domain.example.com/reset-password");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.message")
                .value("If the email exists, a reset link has been sent."));
  }

  /**
   * Test multiple consecutive password reset requests (rate limiting scenario).
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Multiple password reset requests")
  void multiplePasswordResetRequests(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail("user@example.com");
    request.setFrontendUrl("https://app.example.com/reset");

    for (int i = 0; i < 3; i++) {
      this.mockMvc
          .perform(
              MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                  .headers(this.getHttpHeaders())
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(this.objectMapper.writeValueAsString(request)))
          .andExpect(MockMvcResultMatchers.status().isOk());
    }
  }

  /**
   * Test password reset request with the user not found message.
   *
   * @throws Exception if an error occurs during the test
   */
  @Test
  @DisplayName("Request password reset with user not found")
  void requestPasswordResetWithUserNotFound(TestInfo testInfo) throws Exception {
    log.info(TestConstants.TEST_RUNNING, testInfo.getDisplayName());

    PasswordResetRequest request = new PasswordResetRequest();
    request.setEmail("test_@example.com");
    request.setFrontendUrl("https://custom-domain.example.com/reset-password");

    this.mockMvc
        .perform(
            MockMvcRequestBuilders.post(PASSWORD_BASE_PATH)
                .headers(this.getHttpHeaders())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User not found"));
  }
}
