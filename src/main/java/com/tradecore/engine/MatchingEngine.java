package com.tradecore.engine;

import com.tradecore.model.Order;
import com.tradecore.model.Trade;
import com.tradecore.strategy.MatchingStrategy;

import java.util.List;

public class MatchingEngine {

    private static final MatchingEngine INSTANCE = new MatchingEngine();

    private final OrderBook orderBook;
    private MatchingStrategy matchingStrategy;

    private MatchingEngine() {
        this.orderBook = new OrderBook();
    }

    public static MatchingEngine getInstance() {
        return INSTANCE;
    }

    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    public void submitOrder(Order order) {
        orderBook.addOrder(order);
    }

    public List<Trade> match() {
        if (matchingStrategy == null) {
            throw new IllegalStateException("MatchingStrategy not set");
        }
        return matchingStrategy.match(orderBook);
    }
}