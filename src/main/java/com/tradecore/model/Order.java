package com.tradecore.model;

import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderStatus;

public abstract class Order implements Tradeable {

    protected final String orderId;
    protected final String symbol;
    protected final int quantity;
    protected final OrderSide side;
    protected OrderStatus status;

    protected Order(String orderId, String symbol, int quantity, OrderSide side) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.side = side;
        this.status = OrderStatus.PENDING;
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
}