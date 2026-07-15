package com.moontech.archetype.infrastructure.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Configuration for password encryption.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Configuration
public class PasswordEncoder {
  /**
   * Password encryption.
   *
   * @return {@code BCryptPasswordEncoder}
   */
  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }
}
