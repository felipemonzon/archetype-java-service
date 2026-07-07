package com.moontech.archetype.infrastructure.security.config;

import com.moontech.archetype.application.business.SecurityBusiness;
import com.moontech.archetype.infrastructure.security.filter.JwtAuthenticationFilter;
import com.moontech.archetype.infrastructure.security.filter.JwtAuthorizationFilter;
import com.moontech.archetype.infrastructure.security.property.SecurityProperties;
import java.util.Arrays;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Spring Security configuration.
 *
 * @author Felipe Monzón
 * @since 2026-06-10
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
  /** Security properties {@code SecurityProperties}. */
  private final SecurityProperties securityProperties;

  /** User service. */
  private final SecurityBusiness securityBusiness;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .cors(
            cors ->
                cors.configurationSource(
                    request -> {
                      CorsConfiguration config = new CorsConfiguration();
                      config.setAllowedOrigins(this.securityProperties.getCors());
                      config.addAllowedHeader(CorsConfiguration.ALL);
                      config.setAllowedMethods(
                          Arrays.asList(
                              HttpMethod.POST.name(),
                              HttpMethod.GET.name(),
                              HttpMethod.PUT.name(),
                              HttpMethod.DELETE.name(),
                              HttpMethod.OPTIONS.name()));
                      config.setExposedHeaders(Collections.singletonList(CorsConfiguration.ALL));
                      return config;
                    }))
        .headers(
            headers ->
                headers
                    .xssProtection(
                        xss ->
                            xss.headerValue(
                                XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
                    .contentSecurityPolicy(cps -> cps.policyDirectives("default-src 'self'"))
                    .httpStrictTransportSecurity(
                        sts -> sts.includeSubDomains(Boolean.TRUE).maxAgeInSeconds(36L)))
        .authorizeHttpRequests(
            authorize ->
                authorize.requestMatchers(WHITELIST).permitAll().anyRequest().authenticated())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(
            new JwtAuthenticationFilter(
                this.authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                this.securityProperties.getJwtKey(),
                this.securityProperties.getJwtLifeTime(),
                PathPatternRequestMatcher.withDefaults()
                    .matcher(
                        HttpMethod.POST,
                        this.securityProperties.getUserAuthenticationPath()),
                this.securityProperties.getRefreshTokenLifeTime()),
            UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(
            this.jwtAuthorizationFilterBean(), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Initializes the filter for token-based authorization.
   *
   * @return token filter
   */
  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilterBean() {
    return new JwtAuthorizationFilter(this.securityProperties, this.securityBusiness);
  }

  /** Whitelist of endpoints. */
  private static final String[] WHITELIST = {
    "/v3/api-docs/**",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/health",
    "/actuator/health/**",
    "/actuator/info/**",
    "/actuator/prometheus"
  };
}