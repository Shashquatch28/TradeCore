package com.tradecore.engine;

import com.tradecore.enums.OrderSide;
import com.tradecore.model.Order;

import java.util.PriorityQueue;

public class OrderBook {

    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;

    public OrderBook() {
        this.buyOrders = new PriorityQueue<>(
                (o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice())
        );

        this.sellOrders = new PriorityQueue<>(
                (o1, o2) -> Double.compare(o1.getPrice(), o2.getPrice())
        );
    }

    public void addOrder(Order order) {
        if (order.getPrice() <= 0) {
            throw new IllegalArgumentException("Order price must be positive");
        }

        if (order.getSide() == OrderSide.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public Order getBestBuy() {
        return buyOrders.peek();
    }

    public Order getBestSell() {
        return sellOrders.peek();
    }

    public boolean hasBuyOrders() {
        return !buyOrders.isEmpty();
    }

    public boolean hasSellOrders() {
        return !sellOrders.isEmpty();
    }
}