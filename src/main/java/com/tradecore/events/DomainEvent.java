package com.tradecore.events;

import java.time.Instant;

/**
 * Marker interface for all domain events.
 * All events must be immutable.
 */
public interface DomainEvent {
    Instant occurredAt();
}