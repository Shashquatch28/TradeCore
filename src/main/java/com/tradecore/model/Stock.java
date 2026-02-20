package com.tradecore.model;

import com.tradecore.engine.OrderBook;

/**
 * Aggregate root representing a tradable stock.
 */
public class Stock {

    private final String symbol;
    private double currentPrice;
    private final OrderBook orderBook;

    public Stock(String symbol, double initialPrice) {
        this.symbol = symbol;
        this.currentPrice = initialPrice;
        this.orderBook = new OrderBook();
    }

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Updates the latest market price (used by PriceTickEvent).
     */
    public void updatePrice(double price) {
        this.currentPrice = price;
    }

    public OrderBook getOrderBook() {
        return orderBook;
    }
}