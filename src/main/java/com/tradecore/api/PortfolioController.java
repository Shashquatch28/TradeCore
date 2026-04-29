package com.tradecore.api;

import com.tradecore.trader.Trader;
import com.tradecore.registry.TraderRegistry;
import com.tradecore.model.Portfolio;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portfolio")
public class PortfolioController {

    private final TraderRegistry traderRegistry;

    public PortfolioController(TraderRegistry traderRegistry) {
        this.traderRegistry = traderRegistry;
    }

    @GetMapping("/{traderId}")
    public Portfolio getPortfolio(@PathVariable String traderId) {
        Trader trader = traderRegistry.getTrader(traderId);
        return trader.getPortfolio();
    }
}