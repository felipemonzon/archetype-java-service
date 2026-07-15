package com.moontech.archetype.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moontech.archetype.commons.enums.Genre;
import com.moontech.archetype.commons.enums.Status;
import com.moontech.archetype.constants.TestConstants;
import com.moontech.archetype.infrastructure.model.response.SecurityResponse;
import com.moontech.archetype.infrastructure.security.constant.SecurityConstants;
import com.moontech.archetype.infrastructure.security.property.SecurityProperties;
import com.moontech.archetype.infrastructure.security.utility.SecurityUtilities;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Base configuration for integration tests. This class provides a common setup for Spring Boot
 * tests, including: - Loading the full Spring application context. - Auto-configuring MockMvc for
 * testing web layers. - Activating the "test" Spring profile. - Providing a mock authenticated user
 * with ADMIN role. - Setting up MockMvc with Spring Security support.
 *
 * @author Felipe Monzón
 * @since 2026-07-13
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Tag("integration-test")
@WithMockUser(roles = TestConstants.ROLE_ADMIN)
public abstract class BaseTestConfiguration extends MysqlBaseConfigurationTest {

  /** Web application context for setting up MockMvc. */
  @Autowired protected WebApplicationContext webApplicationContext;

  @Autowired private SecurityProperties securityProperties;

  /** MockMvc instance for performing HTTP requests in tests. */
  protected MockMvc mockMvc;

  /** ObjectMapper for JSON serialization/deserialization in tests. */
  protected ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Initializes MockMvc with Spring Security support before each test. This ensures that security
   * filters are applied during test requests.
   */
  @BeforeEach
  void init() {
    log.info("Starting tests...");
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
  }

  protected HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add(
        HttpHeaders.AUTHORIZATION,
        SecurityConstants.TOKEN_BEARER_PREFIX + StringUtils.SPACE + this.generateTestToken());
    headers.add(TestConstants.UUID_HEADER, String.valueOf(UUID.randomUUID()));
    headers.add(SecurityConstants.REFRESH_TOKEN_HEADER_NAME, TestConstants.TEST_TOKEN);

    return headers;
  }

  protected String generateTestToken() {
    var auth =
        SecurityUtilities.getAuthentication(
            TestConstants.TEST_TOKEN,
            this.getSecurityResponse(),
            this.securityProperties.getJwtKey());
    return SecurityUtilities.generateToken(
        auth, this.securityProperties.getJwtKey(), this.securityProperties.getJwtLifeTime());
  }

  protected SecurityResponse getSecurityResponse() {
    SecurityResponse securityResponse = new SecurityResponse();
    securityResponse.setPhone(StringUtils.EMPTY);
    securityResponse.setStatus(Status.ACTIVE);
    securityResponse.setGenre(Genre.OTHER);
    securityResponse.setUsername(TestConstants.USERNAME);
    return securityResponse;
  }
}
