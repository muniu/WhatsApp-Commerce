package co.ke.whatsappcommerce.events;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public abstract class BaseEvent<T> implements Event<T> {
    private final String eventId = UUID.randomUUID().toString();
    private final LocalDateTime timestamp = LocalDateTime.now();
    private final EventType eventType;
    private final T payload;

    protected BaseEvent(EventType eventType, T payload) {
        this.eventType = eventType;
        this.payload = payload;
    }
}

