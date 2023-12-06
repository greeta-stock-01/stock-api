package net.greeta.blog.model;

import java.util.UUID;

public class Event {
    private final UUID id;
    private final String eventType;

    public Event(UUID id, String eventType) {
        this.id = id;
        this.eventType = eventType;
    }

    public UUID getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }
}
