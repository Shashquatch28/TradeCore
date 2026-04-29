package com.tradecore.config;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.strategy.FIFOMatchingStrategy;
import com.tradecore.persistence.TradePersistenceListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineConfig {

    @Bean
    public MatchingEngine matchingEngine(TradePersistenceListener listener) {

       MatchingEngine engine = new MatchingEngine();
       engine.setMatchingStrategy(new FIFOMatchingStrategy());

        engine.getEventBus().subscribe(
            com.tradecore.events.TradeExecutedEvent.class,
                listener::onTradeExecuted
        );

        return engine;
    }
}