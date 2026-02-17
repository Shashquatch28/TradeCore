package com.tradecore.events;

import com.tradecore.model.Trade;

import java.time.Instant;

public final class TradeExecutedEvent implements DomainEvent {

    private final Trade trade;
    private final Instant occurredAt = Instant.now();

    public TradeExecutedEvent(Trade trade) {
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }

    @Override
    public Instant occurredAt() {
        return occurredAt;
    }
}