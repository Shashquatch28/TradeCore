package com.tradecore.marketdata;

import java.time.Instant;

public final class MarketPrice {

    private final String symbol;
    private final double price;
    private final Instant timestamp;

    public MarketPrice(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
        this.timestamp = Instant.now();
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}