package com.tradecore.api;

import com.tradecore.api.dto.MarketOrderRequest;
import com.tradecore.api.dto.LimitOrderRequest;
import com.tradecore.engine.MatchingEngine;
import com.tradecore.model.*;
import com.tradecore.trader.Trader;
import com.tradecore.registry.TraderRegistry;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final MatchingEngine engine;
    private final TraderRegistry traderRegistry;

    public OrderController(MatchingEngine engine,
                           TraderRegistry traderRegistry) {
        this.engine = engine;
        this.traderRegistry = traderRegistry;
    }

    @PostMapping("/market")
    public String placeMarket(@RequestBody MarketOrderRequest req) {

        // ✅ Get existing trader
        Trader trader = traderRegistry.getTrader(req.traderId);

        if (trader == null) {
            return "Trader not found. Create user first.";
        }

        MarketOrder order = new MarketOrder(
                UUID.randomUUID().toString(),
                req.symbol,
                req.quantity,
                req.side,
                trader
        );

        engine.submitOrder(order);

        return "Market order placed";
    }

    @PostMapping("/limit")
    public String placeLimit(@RequestBody LimitOrderRequest req) {

        Trader trader = traderRegistry.getTrader(req.traderId);

        if (trader == null) {
            return "Trader not found. Create user first.";
        }

        LimitOrder order = new LimitOrder(
                UUID.randomUUID().toString(),
                req.symbol,
                req.quantity,
                req.side,
                req.price,
                trader
        );

        engine.submitOrder(order);

        return "Limit order placed";
    }
}