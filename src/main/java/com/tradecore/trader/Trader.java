package com.tradecore.trader;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Order;
import com.tradecore.model.Portfolio;

/**
 * Base class for all traders.
 * Represents a trading participant with identity and portfolio.
 */
public abstract class Trader {

    protected final String traderId;
    protected final String name;

    protected Trader(String traderId, String name) {
        if (traderId == null || traderId.isBlank()) {
            throw new IllegalArgumentException("traderId cannot be null or empty");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }

        this.traderId = traderId;
        this.name = name;
    }

    /**
     * Place an order into the system.
     */
    public abstract void placeOrder(Order order);

    /**
     * Enforces portfolio access via abstraction.
     */
    public abstract Portfolio getPortfolio();

    /**
     * Event handler hook for trade execution.
     * Can be overridden by subclasses if needed.
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

    @Override
    public String toString() {
        return "Trader{" +
                "id='" + traderId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}