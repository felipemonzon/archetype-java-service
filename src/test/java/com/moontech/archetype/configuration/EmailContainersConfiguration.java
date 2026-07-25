package com.moontech.archetype.configuration;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Test configuration that starts a local SMTP server via Mailpit for mail-related tests.
 *
 * <p>This allows the application to send mail without requiring a real external provider.
 *
 * @author Felipe Monzón
 * @since 2026-07-27
 */
@Testcontainers
@ActiveProfiles("test")
public class EmailContainersConfiguration {
  /** Mailpit container exposing SMTP and HTTP API ports. */
  @Container @ServiceConnection
  static final GenericContainer<?> mailpit =
      new GenericContainer<>("axllent/mailpit:latest")
          .withExposedPorts(1025); // 1025: SMTP, 8025: HTTP API

  /**
   * Registers dynamic mail properties for the test context.
   *
   * @param registry dynamic property registry
   */
  @DynamicPropertySource
  static void configureMailProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.mail.host", mailpit::getHost);
    registry.add("spring.mail.port", () -> mailpit.getMappedPort(1025));
    registry.add("spring.mail.username", () -> "");
    registry.add("spring.mail.password", () -> "");
    registry.add("spring.mail.properties.mail.smtp.auth", () -> "false");
    registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> "false");
  }
}
