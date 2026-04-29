package com.tradecore;

import com.tradecore.events.EventBus;
import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.portfolio.PortfolioUpdater;
import com.tradecore.registry.TraderRegistry;
import com.tradecore.trader.MarketMaker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

@SpringBootApplication
public class TradeCoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(TradeCoreApplication.class, args);
    }

    /**
     * 🔥 Step 1: Register Market Maker
     */
    @Bean
    @Order(1)
    public CommandLineRunner init(TraderRegistry registry) {
        return args -> {
            registry.registerTrader(new MarketMaker("MM"));
            System.out.println("✅ Market Maker registered");
        };
    }

    /**
     * 🔥 Step 2: Create and wire PortfolioUpdater EARLY
     * (NO CommandLineRunner here)
     */
    @Bean
    public PortfolioUpdater portfolioUpdater(
            EventBus eventBus,
            TraderRegistry registry
    ) {

        PortfolioUpdater updater = new PortfolioUpdater(registry);

        eventBus.subscribe(TradeExecutedEvent.class, updater::onTradeExecuted);

        System.out.println("✅ PortfolioUpdater wired (early)");

        return updater;
    }
}