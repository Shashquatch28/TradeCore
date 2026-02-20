package com.tradecore.metrics;

import com.tradecore.events.TradeExecutedEvent;
import java.util.concurrent.atomic.AtomicLong;

public class EventMetricsListener {

    private final AtomicLong tradesExecuted = new AtomicLong();

    public void onTradeExecuted(TradeExecutedEvent event) {
        tradesExecuted.incrementAndGet();
    }

    public long getTradesExecuted() {
        return tradesExecuted.get();
    }
}