package com.tradecore.api;

import com.tradecore.api.dto.MarketOrderRequest;
import com.tradecore.api.dto.LimitOrderRequest;
import com.tradecore.engine.MatchingEngine;
import com.tradecore.model.*;
import com.tradecore.trader.RetailTrader;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final MatchingEngine engine;

    public OrderController(MatchingEngine engine) {
        this.engine = engine;
    }

    @PostMapping("/market")
    public String placeMarket(@RequestBody MarketOrderRequest req) {

        MarketOrder order = new MarketOrder(
                UUID.randomUUID().toString(),
                req.symbol,
                req.quantity,
                req.side,
                new RetailTrader(req.traderId, "User", 100000.0)
        );

        engine.submitOrder(order);
        return "Market order placed";
    }

    @PostMapping("/limit")
    public String placeLimit(@RequestBody LimitOrderRequest req) {

        LimitOrder order = new LimitOrder(
                UUID.randomUUID().toString(),
                req.symbol,
                req.quantity,
                req.side,
                req.price,
                new RetailTrader(req.traderId, "User", 100000.0)
        );

        engine.submitOrder(order);
        return "Limit order placed";
    }
}