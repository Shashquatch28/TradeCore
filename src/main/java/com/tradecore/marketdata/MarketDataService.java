package com.tradecore.marketdata;

import com.tradecore.events.EventBus;
import com.tradecore.events.PriceTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Periodically polls market prices and emits PriceTickEvent.
 */
public class MarketDataService {

    private static final Logger log =
            LoggerFactory.getLogger(MarketDataService.class);

    private final MarketDataProvider provider;
    private final EventBus eventBus;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

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

                // 🔔 Publish flattened price tick event
                eventBus.publish(
                        new PriceTickEvent(
                                price.getSymbol(),
                                price.getPrice()
                        )
                );

                log.info(
                        "Price tick: {} @ {}",
                        price.getSymbol(),
                        price.getPrice()
                );

            } catch (Exception e) {
                log.warn("Price fetch failed for {}", symbol, e);
            }
        }
    }

    public void stop() {
        scheduler.shutdownNow();
        log.info("MarketDataService stopped");
    }
}