package com.tradecore.model;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.enums.OrderSide;
import com.tradecore.events.OrderPlacedEvent;
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
    public void process(MatchingEngine engine, Stock stock) {
        stock.getOrderBook().addOrder(this);
        engine.getEventBus().publish(new OrderPlacedEvent(this));
        engine.match(stock.getSymbol());
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