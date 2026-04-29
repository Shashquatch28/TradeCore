package com.tradecore.config;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.enums.OrderSide;
import com.tradecore.model.LimitOrder;
import com.tradecore.registry.TraderRegistry;
import com.tradecore.trader.Trader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.UUID;

@Configuration
public class LiquidityInitializer {

    @Bean
    @Order(2) // 🔥 RUN AFTER MarketMaker registration
    public CommandLineRunner seedLiquidity(
            MatchingEngine engine,
            TraderRegistry registry
    ) {
        return args -> {

            Trader mm = registry.getTrader("MM");

            if (mm == null) {
                throw new RuntimeException("Market Maker not found");
            }

            // ================= SELL SIDE =================
            for (int i = 95; i <= 105; i++) {
                engine.submitOrder(new LimitOrder(
                        UUID.randomUUID().toString(),
                        "AAPL",
                        100,
                        OrderSide.SELL,
                        i,
                        mm
                ));
            }

            // ================= BUY SIDE =================
            for (int i = 94; i >= 85; i--) {
                engine.submitOrder(new LimitOrder(
                        UUID.randomUUID().toString(),
                        "AAPL",
                        100,
                        OrderSide.BUY,
                        i,
                        mm
                ));
            }

            System.out.println("💧 Liquidity seeded for AAPL");
        };
    }
}