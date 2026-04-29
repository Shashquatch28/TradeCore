package com.tradecore.marketdata;

import com.tradecore.events.EventBus;
import com.tradecore.events.PriceTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

/**
 * Periodically polls market prices and emits PriceTickEvent.
 * Supports fallback simulation for robustness.
 */
public class MarketDataService {

    private static final Logger log =
            LoggerFactory.getLogger(MarketDataService.class);

    private final MarketDataProvider provider;
    private final EventBus eventBus;

    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    // 🔥 Maintain last known prices for smooth simulation fallback
    private final Map<String, Double> lastPrices = new ConcurrentHashMap<>();

    public MarketDataService(
            MarketDataProvider provider,
            EventBus eventBus
    ) {
        this.provider = provider;
        this.eventBus = eventBus;
    }

    public void start(List<String> symbols, Duration interval) {

        log.info(
                "Starting MarketDataService for symbols={} interval={}s",
                symbols,
                interval.toSeconds()
        );

        scheduler.scheduleAtFixedRate(
                () -> poll(symbols),
                0,
                interval.toSeconds(),
                TimeUnit.SECONDS
        );
    }

    private void poll(List<String> symbols) {

        for (String symbol : symbols) {
            try {

                MarketPrice price = provider.fetchPrice(symbol);

                double value = price.getPrice();

                lastPrices.put(symbol, value);

                publish(symbol, value);

                log.debug("LIVE {} @ {}", symbol, value);

            } catch (Exception e) {

                log.warn("Live fetch failed for {}, switching to simulation", symbol);

                double fallback = simulatePrice(symbol);

                publish(symbol, fallback);
            }
        }
    }

    private void publish(String symbol, double price) {
        eventBus.publish(
                new PriceTickEvent(symbol, price)
        );
    }

    /**
     * 🔥 Random-walk simulation for fallback
     */
    private double simulatePrice(String symbol) {

        double base = lastPrices.getOrDefault(symbol, 100.0);

        double change = (Math.random() - 0.5) * 2; // -1 to +1

        double newPrice = Math.max(1, base + change);

        lastPrices.put(symbol, newPrice);

        return newPrice;
    }

    public void stop() {
        scheduler.shutdownNow();
        log.info("MarketDataService stopped");
    }
}