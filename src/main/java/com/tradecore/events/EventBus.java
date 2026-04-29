package com.tradecore.events;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Simple in-memory synchronous event bus.
 */
@Component   // REQUIRED: makes this a Spring Bean
public class EventBus {

    private final Map<Class<? extends DomainEvent>,
            List<Consumer<? extends DomainEvent>>> handlers = new ConcurrentHashMap<>();

    public <T extends DomainEvent> void subscribe(
            Class<T> eventType,
            Consumer<T> handler
    ) {
        handlers
                .computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()) // 🔥 thread-safe
                .add(handler);
    }

    @SuppressWarnings("unchecked")
    public void publish(DomainEvent event) {
        List<Consumer<? extends DomainEvent>> eventHandlers =
                handlers.getOrDefault(event.getClass(), List.of());

        for (Consumer<? extends DomainEvent> handler : eventHandlers) {
            try {
                ((Consumer<DomainEvent>) handler).accept(event);
            } catch (Exception e) {
                System.err.println("❌ Event handler failed: " + e.getMessage());
            }
        }
    }
}