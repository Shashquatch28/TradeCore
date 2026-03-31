package com.tradecore.portfolio;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Trade;
import com.tradecore.registry.TraderRegistry;
import com.tradecore.trader.Trader;

/**
 * Updates trader portfolios upon trade execution.
 * Uses polymorphism instead of instanceof checks.
 */
public class PortfolioUpdater {

    private final TraderRegistry traderRegistry;

    public PortfolioUpdater(TraderRegistry traderRegistry) {
        this.traderRegistry = traderRegistry;
    }

    public void onTradeExecuted(TradeExecutedEvent event) {

        Trade trade = event.getTrade();

        Trader buyer = traderRegistry.getTrader(trade.getBuyerId());
        Trader seller = traderRegistry.getTrader(trade.getSellerId());

        if (buyer != null) {
            buyer.getPortfolio().applyTrade(
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    true
            );
        }

        if (seller != null) {
            seller.getPortfolio().applyTrade(
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    false
            );
        }
    }
}