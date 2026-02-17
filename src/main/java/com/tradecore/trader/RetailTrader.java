package com.tradecore.trader;

import com.tradecore.model.Order;
import com.tradecore.model.Portfolio;
import com.tradecore.model.Trade;

public class RetailTrader extends Trader {

    private final Portfolio portfolio;

    public RetailTrader(String traderId, String name, double initialCash) {
        super(traderId, name);
        this.portfolio = new Portfolio(initialCash);
    }

    @Override
    public void placeOrder(Order order) {
        // Order submission will be handled by MatchingEngine
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    @Override
    public void onTradeExecuted(Trade trade) {
        // TEMPORARY: assume buy side
        // Will be fixed when order ownership is introduced
        portfolio.applyTrade(
                trade.getSymbol(),
                trade.getQuantity(),
                trade.getPrice(),
                true
        );
    }
}