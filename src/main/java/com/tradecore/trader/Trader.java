package com.tradecore.trader;

import com.tradecore.model.Order;

public abstract class Trader {

    protected final String traderId;
    protected final String name;

    protected Trader(String traderId, String name) {
        this.traderId = traderId;
        this.name = name;
    }

    public abstract void placeOrder(Order order);

    public String getTraderId() {
        return traderId;
    }

    public String getName() {
        return name;
    }
}