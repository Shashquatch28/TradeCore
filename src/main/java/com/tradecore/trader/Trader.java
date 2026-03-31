package com.tradecore.trader;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Order;
import com.tradecore.model.Portfolio;

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
     * 🔥 NEW: Enforce portfolio access via abstraction
     */
    public abstract Portfolio getPortfolio();

    /**
     * Event handler hook.
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