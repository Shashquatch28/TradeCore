package com.tradecore.trader;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Order;

/**
 * Base class for all traders.
 * Traders can optionally react to trade execution events.
 */
public abstract class Trader {

    protected final String traderId;
    protected final String name;

    protected Trader(String traderId, String name) {
        this.traderId = traderId;
        this.name = name;
    }

    public abstract void placeOrder(Order order);

    /**
     * Event handler hook.
     * Subclasses may override if they need to react to trades.
     */
    public void onTradeExecuted(TradeExecutedEvent event) {
        // default: no-op
    }

    public String getTraderId() {
        return traderId;
    }

    public String getName() {
        return name;
    }
}