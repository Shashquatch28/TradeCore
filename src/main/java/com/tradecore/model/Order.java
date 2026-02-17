package com.tradecore.model;

import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderStatus;
import com.tradecore.trader.Trader;


public abstract class Order implements Tradeable {

    protected final String orderId;
    protected final String symbol;
    protected int quantity;
    protected final OrderSide side;
    protected OrderStatus status;
    protected final Trader trader;

    protected Order(String orderId, String symbol, int quantity, OrderSide side, Trader trader) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.side = side;
        this.status = OrderStatus.PENDING;
        this.trader = trader;
    }

    public abstract void execute();

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public OrderSide getSide() {
        return side;
    }

    public int getQuantity() {
        return quantity;
    }
    
    public String getOrderId() {
        return orderId;
    }

    public void reduceQuantity(int amount) {
        if (amount <= 0 || amount > quantity) {
            throw new IllegalArgumentException("Invalid reduction amount");
        }
    
        quantity -= amount;
    
        if (quantity == 0) {
            status = OrderStatus.FILLED;
        } else {
            status = OrderStatus.PARTIAL;
        }
    }

    public Trader getTrader() {
        return trader;
    }
}