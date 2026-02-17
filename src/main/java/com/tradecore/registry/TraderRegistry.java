package com.tradecore.registry;

import com.tradecore.trader.Trader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for all traders in the system.
 * Maps traderId -> Trader instance.
 */
public class TraderRegistry {

    private final Map<String, Trader> traders = new ConcurrentHashMap<>();

    public void registerTrader(Trader trader) {
        traders.put(trader.getTraderId(), trader);
    }

    public Trader getTrader(String traderId) {
        return traders.get(traderId);
    }

    public boolean contains(String traderId) {
        return traders.containsKey(traderId);
    }
}