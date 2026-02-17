package com.tradecore.model;

import java.time.Instant;

public final class Trade {

    private final String symbol;
    private final double price;
    private final int quantity;
    private final Instant timestamp;

    public Trade(String symbol, double price, int quantity) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = Instant.now();
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}