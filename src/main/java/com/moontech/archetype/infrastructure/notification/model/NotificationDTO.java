package com.moontech.archetype.infrastructure.notification.model;

import com.moontech.archetype.infrastructure.notification.enums.EmailTemplate;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

/**
 * Data transfer object for email notifications.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Getter
@Builder
public class NotificationDTO {
  /** Recipient email address. */
  private String to;

  /** Template model containing variables for rendering. */
  private Map<String, String> model;

  /** Email template to use for rendering. */
  private EmailTemplate templateName;
}
