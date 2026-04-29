package com.tradecore.api;

import com.tradecore.registry.TraderRegistry;
import com.tradecore.trader.Trader;
import com.tradecore.model.Portfolio;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final TraderRegistry registry;

    public PortfolioController(TraderRegistry registry) {
        this.registry = registry;
    }

    @GetMapping("/{traderId}")
    public Portfolio getPortfolio(@PathVariable String traderId) {

        Trader trader = registry.getTrader(traderId);

        if (trader == null) {
            throw new RuntimeException("Trader not found: " + traderId);
        }

        return trader.getPortfolio();
    }
}