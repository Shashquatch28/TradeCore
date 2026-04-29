package com.tradecore.api;

import com.tradecore.trader.*;
import org.springframework.web.bind.annotation.*;
import com.tradecore.registry.TraderRegistry;

@RestController
@RequestMapping("/users")
public class UserController {

    private final TraderRegistry registry;

    public UserController(TraderRegistry registry) {
        this.registry = registry;
    }

    @PostMapping("/create")
    public String createUser(@RequestBody UserRequest req) {

        Trader trader = new RetailTrader(
                req.id,
                req.id,
                100000.0
        );

        registry.registerTrader(trader);

        return "User created";
    }

    static class UserRequest {
        public String id;
    }
}