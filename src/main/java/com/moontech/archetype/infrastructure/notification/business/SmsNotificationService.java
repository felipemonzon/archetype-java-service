package com.moontech.archetype.infrastructure.notification.business;

import com.moontech.archetype.infrastructure.notification.enums.NotificationChannel;
import com.moontech.archetype.infrastructure.notification.model.NotificationDTO;
import com.moontech.archetype.infrastructure.notification.strategy.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Dummy SMS notification strategy. Replace with real provider implementation (Twilio, Nexmo, etc.).
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Slf4j
@Component
public class SmsNotificationService implements NotificationStrategy {
  /**
   * Returns the SMS notification channel.
   *
   * @return the SMS channel
   */
  @Override
  public NotificationChannel channel() {
    return NotificationChannel.SMS;
  }

  /**
   * Sends an SMS notification (dummy implementation that logs the content).
   *
   * @param request the notification DTO
   */
  @Override
  public void send(NotificationDTO request) {
    log.info("[SMS] to={} body={}", request.getTo(), request.getModel());
  }
}
