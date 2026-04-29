package com.tradecore.model;

import com.tradecore.engine.MatchingEngine;
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

    @Override
    public void process(MatchingEngine engine, Stock stock) {
        engine.registerStopLossOrder(this);
    }

    public double getStopPrice() {
        return stopPrice;
    }

    @Override
    public void execute() {
        // Execution handled by MatchingEngine after trigger
    }

    @Override
    public double getPrice() {
        return stopPrice;
    }
}