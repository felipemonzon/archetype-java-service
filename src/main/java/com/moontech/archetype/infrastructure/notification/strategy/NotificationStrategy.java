package com.moontech.archetype.infrastructure.notification.strategy;

import com.moontech.archetype.infrastructure.notification.enums.NotificationChannel;
import com.moontech.archetype.infrastructure.notification.model.NotificationDTO;

/**
 * Strategy interface for notification channels.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
public interface NotificationStrategy {
  /**
   * Returns the notification channel this strategy handles.
   *
   * @return the notification channel
   */
  NotificationChannel channel();

  /**
   * Sends a notification using this strategy.
   *
   * @param request the notification DTO containing message details
   */
  void send(NotificationDTO request);
}
