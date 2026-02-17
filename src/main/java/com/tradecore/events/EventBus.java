package com.tradecore.events;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Simple in-memory synchronous event bus.
 */
public class EventBus {

    private final Map<Class<? extends DomainEvent>,
            List<Consumer<? extends DomainEvent>>> handlers = new ConcurrentHashMap<>();


    public <T extends DomainEvent> void subscribe(
            Class<T> eventType,
            Consumer<T> handler
    ) {
        handlers
                .computeIfAbsent(eventType, k -> new ArrayList<>())
                .add(handler);
    }

    @SuppressWarnings("unchecked")
    public void publish(DomainEvent event) {
        List<Consumer<? extends DomainEvent>> eventHandlers =
                handlers.getOrDefault(event.getClass(), List.of());

        for (Consumer handler : eventHandlers) {
            handler.accept(event);
        }
    }
}