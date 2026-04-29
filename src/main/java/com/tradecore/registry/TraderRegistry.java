package com.tradecore.registry;

import com.tradecore.trader.Trader;
import com.tradecore.exception.InvalidOrderException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for all traders in the system.
 * Maps traderId -> Trader instance.
 */
@Component
public class TraderRegistry {

    private final Map<String, Trader> traders = new ConcurrentHashMap<>();

    /**
     * Registers a new trader.
     * Enforces uniqueness of traderId.
     */
    public void registerTrader(Trader trader) {

        if (trader == null || trader.getTraderId() == null || trader.getTraderId().isBlank()) {
            throw new InvalidOrderException("Invalid trader data");
        }

        if (traders.containsKey(trader.getTraderId())) {
            throw new InvalidOrderException(
                    "Trader already exists: " + trader.getTraderId()
            );
        }

        traders.put(trader.getTraderId(), trader);
    }

    /**
     * Fetch trader by ID.
     */
    public Trader getTrader(String traderId) {

        if (!traders.containsKey(traderId)) {
            throw new InvalidOrderException(
                    "Trader not found: " + traderId
            );
        }

        return traders.get(traderId);
    }

    /**
     * Check if trader exists.
     */
    public boolean contains(String traderId) {
        return traders.containsKey(traderId);
    }
}