package com.tradecore.config;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.events.EventBus;
import com.tradecore.strategy.FIFOMatchingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EngineConfig {

    @Bean
    public MatchingEngine matchingEngine(EventBus eventBus) {
        MatchingEngine engine = new MatchingEngine(eventBus);
        engine.setMatchingStrategy(new FIFOMatchingStrategy());
        return engine;
    }
}