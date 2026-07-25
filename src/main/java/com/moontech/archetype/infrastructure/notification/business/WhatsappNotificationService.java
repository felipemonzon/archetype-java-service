package com.moontech.archetype.infrastructure.notification.business;

import com.moontech.archetype.infrastructure.notification.enums.NotificationChannel;
import com.moontech.archetype.infrastructure.notification.model.NotificationDTO;
import com.moontech.archetype.infrastructure.notification.strategy.NotificationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Dummy WhatsApp notification strategy. Replace with real provider implementation (WhatsApp
 * Business API, Twilio, etc.).
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Slf4j
@Component
public class WhatsappNotificationService implements NotificationStrategy {
  /**
   * Returns the WHATSAPP notification channel.
   *
   * @return the WHATSAPP channel
   */
  @Override
  public NotificationChannel channel() {
    return NotificationChannel.WHATSAPP;
  }

  /**
   * Sends a WhatsApp notification (dummy implementation that logs the content).
   *
   * @param request the notification DTO
   */
  @Override
  public void send(NotificationDTO request) {
    log.info("[WHATSAPP] to={} body={}", request.getTo(), request.getModel());
  }
}
