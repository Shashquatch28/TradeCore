package com.tradecore.model;

import com.tradecore.enums.OrderSide;
import com.tradecore.trader.Trader;

public class LimitOrder extends Order {

    private final double limitPrice;

    public LimitOrder(String orderId,
                      String symbol,
                      int quantity,
                      OrderSide side,
                      double limitPrice,
                      Trader trader) {

        super(orderId, symbol, quantity, side, trader);
        this.limitPrice = limitPrice;
    }

    @Override
    public void execute() {
        // Execution handled by MatchingEngine
    }

    @Override
    public double getPrice() {
        return limitPrice;
    }
}