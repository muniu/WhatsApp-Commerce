package co.ke.whatsappcommerce.events;

import java.time.LocalDateTime;

public interface Event<T> {
    String getEventId();
    EventType getEventType();
    LocalDateTime getTimestamp();
    T getPayload();
}