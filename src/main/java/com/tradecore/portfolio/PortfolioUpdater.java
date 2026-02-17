package com.tradecore.portfolio;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Trade;
import com.tradecore.registry.TraderRegistry;
import com.tradecore.trader.Trader;
import com.tradecore.trader.RetailTrader;
import com.tradecore.trader.InstitutionalTrader;

public class PortfolioUpdater {

    private final TraderRegistry traderRegistry;

    public PortfolioUpdater(TraderRegistry traderRegistry) {
        this.traderRegistry = traderRegistry;
    }

    public void onTradeExecuted(TradeExecutedEvent event) {

        Trade trade = event.getTrade();

        Trader buyer = traderRegistry.getTrader(trade.getBuyerId());
        Trader seller = traderRegistry.getTrader(trade.getSellerId());

        if (buyer instanceof RetailTrader rt) {
            rt.getPortfolio().applyTrade(
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    true
            );
        }

        if (buyer instanceof InstitutionalTrader it) {
            it.getPortfolio().applyTrade(
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    true
            );
        }

        if (seller instanceof RetailTrader rt) {
            rt.getPortfolio().applyTrade(
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    false
            );
        }

        if (seller instanceof InstitutionalTrader it) {
            it.getPortfolio().applyTrade(
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    false
            );
        }
    }
}