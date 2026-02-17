package com.tradecore.model;

import com.tradecore.enums.OrderSide;
import com.tradecore.trader.Trader;

public class StopLossOrder extends Order {

    private final double stopPrice;

    public StopLossOrder(String orderId,
                         String symbol,
                         int quantity,
                         OrderSide side,
                         double stopPrice,
                         Trader trader) {

        super(orderId, symbol, quantity, side, trader);
        this.stopPrice = stopPrice;
    }

    public void trigger() {
        // Convert to market order when stop price is hit
    }

    @Override
    public void execute() {
        // Execution handled by MatchingEngine
    }

    @Override
    public double getPrice() {
        return stopPrice;
    }
}