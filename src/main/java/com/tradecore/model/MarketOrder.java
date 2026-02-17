package com.tradecore.model;

import com.tradecore.enums.OrderSide;
import com.tradecore.trader.Trader;

public class MarketOrder extends Order {

    public MarketOrder(String orderId,
                       String symbol,
                       int quantity,
                       OrderSide side,
                       Trader trader) {

        super(orderId, symbol, quantity, side, trader);
    }

    @Override
    public void execute() {
        // Execution handled by MatchingEngine
    }

    @Override
    public double getPrice() {
        return 0.0; // Market price determined at execution
    }
}