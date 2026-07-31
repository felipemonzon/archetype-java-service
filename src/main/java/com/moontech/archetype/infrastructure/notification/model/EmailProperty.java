package com.moontech.archetype.infrastructure.notification.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.core.io.Resource;

/**
 * Email configuration properties containing templates, settings, and resources.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
@Builder
@AllArgsConstructor
public class EmailProperty {
  /** Welcome email subject. */
  private String welcomeUserSubject;

  /** Password reset email subject. */
  private String resetPasswordSubject;

  /** Forgot password email subject. */
  private String forgotPasswordSubject;

  /** Sender email address. */
  private String mail;

  /** Welcome email message. */
  private String welcomeMessage;

  /** Welcome image resource. */
  private Resource welcomeImg;

  /** Facebook icon resource. */
  private Resource facebookIcon;

  /** Forgot password image resource. */
  private Resource forgotPasswordImg;

  /** Reset password image resource. */
  private Resource resetPasswordImg;

  /** LinkedIn icon resource. */
  private Resource linkedinIcon;

  /** Instagram icon resource. */
  private Resource instagramIcon;

  /** Twitter icon resource. */
  private Resource twitterIcon;

  /** Map of template names to template file paths. */
  private Map<String, String> templates;

  /** Enterprise name for email footer. */
  private String enterpriseName;

  /** Support enterprise email. */
  private String supportEmail;
}
