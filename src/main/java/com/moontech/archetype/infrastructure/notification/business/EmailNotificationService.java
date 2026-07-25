package com.moontech.archetype.infrastructure.notification.business;

import com.moontech.archetype.infrastructure.exception.custom.NotificationException;
import com.moontech.archetype.infrastructure.notification.constants.NotificationConstants;
import com.moontech.archetype.infrastructure.notification.enums.EmailTemplate;
import com.moontech.archetype.infrastructure.notification.enums.NotificationChannel;
import com.moontech.archetype.infrastructure.notification.model.EmailProperty;
import com.moontech.archetype.infrastructure.notification.model.NotificationDTO;
import com.moontech.archetype.infrastructure.notification.strategy.NotificationStrategy;
import freemarker.template.Configuration;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 * Email notification strategy that sends emails using FreeMarker templates.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailNotificationService implements NotificationStrategy {
  private static final String RESET_PASSWORD = "RESET_PASSWORD";

  private static final String FORGOT_PASSWORD = "FORGOT_PASSWORD";

  private static final String REGISTER_USER = "welcome.png";

  private static final String FORGOT_PASSWORD_ICON = "forgot_password.png";

  private static final String RESET_PASSWORD_ICON = "reset_password.png";

  private static final String FACEBOOK2_NAME = "facebook-rounded-gray.png";

  private static final String INSTAGRAM2_ICON = "instagram-rounded-gray.png";

  private static final String LINKEDIN_ICON = "linkedin-rounded-gray.png";

  private static final String TWITTER2_ICON = "twitter-rounded-gray.png";

  /** Java mail sender for sending emails. */
  private final JavaMailSender mailSender;

  /** Email configuration properties. */
  private final EmailProperty emailProperties;

  /** FreeMarker configuration for template processing. */
  private final Configuration freemarkerConfig;

  /**
   * Sends an email notification using a FreeMarker template.
   *
   * @param notificationDTO the notification data containing recipient, template, and model
   * @throws RuntimeException if email sending fails
   */
  @Override
  public void send(final NotificationDTO notificationDTO) {
    MimeMessage mimeMessage = this.mailSender.createMimeMessage();
    try {
      String subject = this.getSubject(notificationDTO.getTemplateName());
      log.info("Sending email by {}", notificationDTO.getTemplateName().name());
      String template =
          this.emailProperties
              .getTemplates()
              .get(notificationDTO.getTemplateName().name().toLowerCase());
      notificationDTO
          .getModel()
          .put(
              NotificationConstants.MAIL_ENTERPRISE_NAME_PROPERTY,
              this.emailProperties.getEnterpriseName());
      notificationDTO
          .getModel()
          .put(
              NotificationConstants.MAIL_SUPPORT_EMAIL_PROPERTY,
              this.emailProperties.getSupportEmail());
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      mimeMessageHelper.setSubject(subject);
      mimeMessageHelper.setFrom(this.emailProperties.getMail());
      mimeMessageHelper.setTo(notificationDTO.getTo());
      mimeMessageHelper.setText(
          this.getContentFromTemplate(notificationDTO.getModel(), template), true);

      this.getImage(notificationDTO.getTemplateName(), mimeMessageHelper);
      this.getIcons(mimeMessageHelper);

      this.mailSender.send(mimeMessageHelper.getMimeMessage());
    } catch (MessagingException ex) {
      log.error(
          "Failed to create mime message for {}: {}", notificationDTO.getTo(), ex.getMessage());
      throw new NotificationException(
          500, "Failed to create mime message"); // Assuming 500 is an appropriate error code
    } catch (Exception ex) {
      log.error(
          "Failed to process template {} for {}: {}",
          notificationDTO.getTemplateName(),
          notificationDTO.getTo(),
          ex.getMessage());
      throw new NotificationException(
          500, "Failed to process template"); // Assuming 500 is an appropriate error code
    }
  }

  /**
   * Adds inline social media icons to the email message.
   *
   * @param mimeMessageHelper the MIME message helper
   * @throws MessagingException if adding inline resources fails
   */
  private void getIcons(MimeMessageHelper mimeMessageHelper) throws MessagingException {
    mimeMessageHelper.addInline(FACEBOOK2_NAME, this.emailProperties.getFacebookIcon());
    mimeMessageHelper.addInline(LINKEDIN_ICON, this.emailProperties.getLinkedinIcon());
    mimeMessageHelper.addInline(TWITTER2_ICON, this.emailProperties.getTwitterIcon());
    mimeMessageHelper.addInline(INSTAGRAM2_ICON, this.emailProperties.getInstagramIcon());
  }

  /**
   * Processes a FreeMarker template with the given model.
   *
   * @param model the template model containing variables for rendering
   * @param template the template file path
   * @return the rendered template as a string
   */
  private String getContentFromTemplate(Map<String, String> model, String template) {
    StringBuilder content = new StringBuilder();
    try {
      content.append(
          FreeMarkerTemplateUtils.processTemplateIntoString(
              this.freemarkerConfig.getTemplate(template), model));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return content.toString();
  }

  /**
   * Retrieves the email subject based on the template type.
   *
   * @param templateName the email template
   * @return the email subject
   */
  private String getSubject(EmailTemplate templateName) {
    return switch (templateName.name()) {
      case RESET_PASSWORD -> this.emailProperties.getResetPasswordSubject();
      case FORGOT_PASSWORD -> this.emailProperties.getForgotPasswordSubject();
      default -> this.emailProperties.getWelcomeUserSubject();
    };
  }

  private void getImage(EmailTemplate templateName, MimeMessageHelper mimeMessageHelper)
      throws MessagingException {
    switch (templateName.name()) {
      case RESET_PASSWORD ->
          mimeMessageHelper.addInline(
              RESET_PASSWORD_ICON, this.emailProperties.getResetPasswordImg());
      case FORGOT_PASSWORD ->
          mimeMessageHelper.addInline(
              FORGOT_PASSWORD_ICON, this.emailProperties.getForgotPasswordImg());
      default -> mimeMessageHelper.addInline(REGISTER_USER, this.emailProperties.getWelcomeImg());
    }
  }

  /**
   * Returns the EMAIL notification channel.
   *
   * @return the EMAIL channel
   */
  @Override
  public NotificationChannel channel() {
    return NotificationChannel.EMAIL;
  }
}
