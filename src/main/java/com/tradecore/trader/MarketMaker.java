package com.tradecore.trader;

import com.tradecore.model.Order;
import com.tradecore.model.Portfolio;

public class MarketMaker extends Trader {

    private final Portfolio portfolio;

    public MarketMaker(String traderId) {
        super(traderId, "MarketMaker");
        this.portfolio = new Portfolio(1_000_000_000); // huge liquidity
    }

    @Override
    public void placeOrder(Order order) {
        // No-op (engine handles it)
    }

    @Override
    public Portfolio getPortfolio() {
        return portfolio;
    }
}