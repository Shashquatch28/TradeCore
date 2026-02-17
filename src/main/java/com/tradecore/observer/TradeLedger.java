package com.tradecore.observer;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Append-only audit log of executed trades.
 * Acts as an event handler for TradeExecutedEvent.
 */
public class TradeLedger {

    private final List<Trade> trades = new ArrayList<>();

    /**
     * Event handler method.
     */
    public void onTradeExecuted(TradeExecutedEvent event) {
        trades.add(event.getTrade());
    }

    /**
     * Read-only view of all trades.
     */
    public List<Trade> getAllTrades() {
        return Collections.unmodifiableList(trades);
    }

    /**
     * Convenience helper (optional).
     */
    public List<Trade> getTradesForSymbol(String symbol) {
        return trades.stream()
                .filter(t -> t.getSymbol().equals(symbol))
                .toList();
    }

    public void clear() {
        trades.clear();
    }
}