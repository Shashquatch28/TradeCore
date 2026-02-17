package com.tradecore.model;

import java.time.Instant;

public final class Trade {

    private final String symbol;
    private final double price;
    private final int quantity;
    private final String buyerId;
    private final String sellerId;
    private final Instant timestamp;

    public Trade(String symbol,
                 double price,
                 int quantity,
                 String buyerId,
                 String sellerId) {

        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
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

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}