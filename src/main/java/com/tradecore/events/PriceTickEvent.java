package com.tradecore.events;

import java.time.Instant;

/**
 * Domain event emitted whenever a new market price is received.
 * Used by engine, metrics, and WebSocket consumers.
 */
public final class PriceTickEvent implements DomainEvent {

    private final String symbol;
    private final double price;
    private final Instant occurredAt = Instant.now();

    public PriceTickEvent(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}