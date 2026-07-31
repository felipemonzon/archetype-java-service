package com.moontech.archetype.infrastructure.notification.config;

import com.moontech.archetype.infrastructure.notification.constants.NotificationConstants;
import com.moontech.archetype.infrastructure.notification.model.EmailProperty;
import java.util.Map;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Configuration properties holder for email templates and resources.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Setter
@Configuration
@ConfigurationProperties(prefix = NotificationConstants.PROPERTIES_MAIL)
public class EmailConfigProperty {
  private String welcomeSubject;

  private String resetPasswordSubject;

  private String forgotPasswordSubject;

  private String from;

  private String enterpriseName;

  /** Support enterprise email. */
  private String supportEmail;

  @Value("classpath:/templates/images/welcome.png")
  private Resource welcomeIcon;

  @Value("classpath:/templates/images/facebook-rounded-gray.png")
  private Resource facebookIcon;

  @Value("classpath:/templates/images/forgot_password.png")
  private Resource forgotPasswordImg;

  @Value("classpath:/templates/images/reset_password.png")
  private Resource resetPasswordImg;

  @Value("classpath:/templates/images/linkedin-rounded-gray.png")
  private Resource linkedinIcon;

  @Value("classpath:/templates/images/instagram-rounded-gray.png")
  private Resource instagramIcon;

  @Value("classpath:/templates/images/twitter-rounded-gray.png")
  private Resource twitterIcon;

  private Map<String, String> templates;

  /**
   * Loads configured email properties into an EmailProperty bean used by the notification system.
   *
   * @return populated EmailProperty instance
   */
  @Bean
  public EmailProperty loadConfig() {
    return EmailProperty.builder()
        .mail(this.from)
        .welcomeImg(this.welcomeIcon)
        .forgotPasswordImg(this.forgotPasswordImg)
        .resetPasswordImg(this.resetPasswordImg)
        .welcomeUserSubject(this.welcomeSubject)
        .resetPasswordSubject(this.resetPasswordSubject)
        .forgotPasswordSubject(this.forgotPasswordSubject)
        .templates(this.templates)
        .facebookIcon(this.facebookIcon)
        .enterpriseName(this.enterpriseName)
        .supportEmail(this.supportEmail)
        .twitterIcon(this.twitterIcon)
        .instagramIcon(this.instagramIcon)
        .linkedinIcon(this.linkedinIcon)
        .build();
  }
}
