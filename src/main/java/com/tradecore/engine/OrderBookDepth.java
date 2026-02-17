package com.tradecore.engine;

import java.util.Map;

public class OrderBookDepth {

    private final Map<Double, Integer> bids;
    private final Map<Double, Integer> asks;

    public OrderBookDepth(Map<Double, Integer> bids, Map<Double, Integer> asks) {
        this.bids = bids;
        this.asks = asks;
    }

    public Map<Double, Integer> getBids() {
        return bids;
    }

    public Map<Double, Integer> getAsks() {
        return asks;
    }
}