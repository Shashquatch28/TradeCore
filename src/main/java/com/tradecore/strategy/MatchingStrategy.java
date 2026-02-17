package com.tradecore.strategy;

import com.tradecore.engine.OrderBook;
import com.tradecore.model.Trade;

import java.util.List;

public interface MatchingStrategy {

    /**
     * Attempts to match orders in the order book.
     * Returns a list of executed trades.
     */
    List<Trade> match(OrderBook orderBook);
}