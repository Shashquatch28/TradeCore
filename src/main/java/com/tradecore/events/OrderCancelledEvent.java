package com.tradecore.events;

import com.tradecore.model.Order;

import java.time.Instant;

public final class OrderCancelledEvent implements DomainEvent {

    private final Order order;
    private final Instant occurredAt = Instant.now();

    public OrderCancelledEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}