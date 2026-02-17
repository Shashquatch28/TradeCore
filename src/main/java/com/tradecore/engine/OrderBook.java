package com.tradecore.engine;

import com.tradecore.enums.OrderSide;
import com.tradecore.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeMap;

public class OrderBook {

    private static final Logger log =
            LoggerFactory.getLogger(OrderBook.class);

    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;

    public OrderBook() {
        this.buyOrders = new PriorityQueue<>(
                (o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice())
        );

        this.sellOrders = new PriorityQueue<>(
                Comparator.comparingDouble(Order::getPrice)
        );
    }

    /* ===================== ORDER OPS ===================== */

    public void addOrder(Order order) {
        if (order.getPrice() <= 0) {
            throw new IllegalArgumentException("Order price must be positive");
        }

        if (order.getSide() == OrderSide.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }

        log.debug("Order added: id={}, side={}, price={}, qty={}",
                order.getOrderId(),
                order.getSide(),
                order.getPrice(),
                order.getQuantity()
        );
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

    /* ===================== CANCEL / LOOKUP ===================== */

    public Order getOrderById(String orderId) {

        for (Order order : buyOrders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }

        for (Order order : sellOrders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }

        return null;
    }

    public boolean cancelOrder(String orderId) {

        Order order = getOrderById(orderId);
        if (order == null) {
            return false;
        }

        order.cancel();

        if (order.getSide() == OrderSide.BUY) {
            buyOrders.remove(order);
        } else {
            sellOrders.remove(order);
        }

        log.info("Order cancelled in book: id={}", orderId);
        return true;
    }

    /* ===================== DEPTH ===================== */

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