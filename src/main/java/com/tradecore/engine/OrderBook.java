package com.tradecore.engine;

import com.tradecore.enums.OrderSide;
import com.tradecore.model.Order;
import java.util.Map;
import java.util.TreeMap;
import java.util.Comparator;

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

    public void removeBestBuy() {
        buyOrders.poll();
    }
    
    public void removeBestSell() {
        sellOrders.poll();
    }

    public boolean cancelOrder(String orderId) {

        // Try cancelling BUY orders
        for (Order order : buyOrders) {
            if (order.getOrderId().equals(orderId)) {
                order.cancel();
                buyOrders.remove(order);
                return true;
            }
        }
    
        // Try cancelling SELL orders
        for (Order order : sellOrders) {
            if (order.getOrderId().equals(orderId)) {
                order.cancel();
                sellOrders.remove(order);
                return true;
            }
        }
    
        return false; // Order not found
    }

    public OrderBookDepth getDepth() {

        Map<Double, Integer> bidDepth = new TreeMap<>(Comparator.reverseOrder());
        Map<Double, Integer> askDepth = new TreeMap<>();
    
        for (Order order : buyOrders) {
            bidDepth.merge(order.getPrice(), order.getQuantity(), Integer::sum);
        }
    
        for (Order order : sellOrders) {
            askDepth.merge(order.getPrice(), order.getQuantity(), Integer::sum);
        }
    
        return new OrderBookDepth(bidDepth, askDepth);
    }
}