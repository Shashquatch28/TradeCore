package com.tradecore;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.marketdata.*;
import com.tradecore.events.EventBus;

import java.time.Duration;
import java.util.List;

/**
 * Optional standalone runner (bypasses Spring Boot).
 * Useful for debugging market data independently.
 */
public class Main {

    public static void main(String[] args) {

        System.out.println("Starting standalone MarketData test...");

        EventBus eventBus = new EventBus();

        MarketDataProvider provider = new FinnhubRestClient(); // or mock

        MarketDataService service =
                new MarketDataService(provider, eventBus);

        List<String> symbols = List.of(
                "AAPL",
                "MSFT",
                "GOOG",
                "TSLA"
        );

        // Log price updates
        eventBus.subscribe(
                com.tradecore.events.PriceTickEvent.class,
                event -> System.out.println(
                        event.getSymbol() + " @ " + event.getPrice()
                )
        );

        service.start(symbols, Duration.ofSeconds(3));
    }
}