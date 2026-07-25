package com.moontech.archetype.infrastructure.notification.business;

import com.moontech.archetype.infrastructure.notification.enums.NotificationChannel;
import com.moontech.archetype.infrastructure.notification.model.NotificationDTO;
import com.moontech.archetype.infrastructure.notification.strategy.NotificationStrategy;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Context service that delegates notification sending to the proper strategy.
 *
 * @author Felipe Monzón
 * @since 2026-07-21
 */
@Service
@RequiredArgsConstructor
public class NotificationService {
  /** List of available notification strategies. */
  private final List<NotificationStrategy> strategies;

  /** Map of channels to their corresponding strategies. */
  private Map<NotificationChannel, NotificationStrategy> strategyMap;

  /** Initializes the strategy map after bean construction. */
  @PostConstruct
  void init() {
    this.strategyMap =
        this.strategies.stream().collect(Collectors.toMap(NotificationStrategy::channel, s -> s));
  }

  /**
   * Sends a notification using the appropriate strategy for the given channel.
   *
   * @param channel the notification channel to use
   * @param request the notification DTO containing message details
   * @throws IllegalArgumentException if no strategy exists for the channel
   */
  public void send(NotificationChannel channel, NotificationDTO request) {
    NotificationStrategy strategy = this.strategyMap.get(channel);
    if (strategy == null) {
      throw new IllegalArgumentException("No notification strategy for channel: " + channel);
    }
    strategy.send(request);
  }
}
