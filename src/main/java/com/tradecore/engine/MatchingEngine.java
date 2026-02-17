package com.tradecore.engine;

import com.tradecore.events.EventBus;
import com.tradecore.events.OrderPlacedEvent;
import com.tradecore.events.OrderCancelledEvent;
import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Order;
import com.tradecore.model.Stock;
import com.tradecore.model.Trade;
import com.tradecore.observer.TradeLedger;
import com.tradecore.strategy.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Central matching engine (Singleton).
 * Emits domain events for all significant state changes.
 */
public class MatchingEngine {

    /* ===================== SINGLETON ===================== */

    private static final MatchingEngine INSTANCE = new MatchingEngine();

    public static MatchingEngine getInstance() {
        return INSTANCE;
    }

    /* ===================== LOGGING ===================== */

    private static final Logger log =
            LoggerFactory.getLogger(MatchingEngine.class);

    /* ===================== CORE STATE ===================== */

    private final StockRegistry stockRegistry;
    private MatchingStrategy matchingStrategy;

    // Phase 3: Event-driven core
    private final EventBus eventBus = new EventBus();

    // Read model
    private final TradeLedger tradeLedger = new TradeLedger();

    private MatchingEngine() {
        this.stockRegistry = new StockRegistry();

        // TradeLedger listens to TradeExecutedEvent
        eventBus.subscribe(
                TradeExecutedEvent.class,
                tradeLedger::onTradeExecuted
        );
    }

    /* ===================== CONFIG ===================== */

    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    /* ===================== ORDER FLOW ===================== */

    public void submitOrder(Order order) {

        log.info(
                "Order submitted: id={}, symbol={}, side={}, qty={}, price={}",
                order.getOrderId(),
                order.getSymbol(),
                order.getSide(),
                order.getQuantity(),
                order.getPrice()
        );

        Stock stock = stockRegistry.getOrCreateStock(
                order.getSymbol(),
                order.getPrice()
        );

        stock.getOrderBook().addOrder(order);

        eventBus.publish(new OrderPlacedEvent(order));
    }

    public boolean cancelOrder(String orderId, String symbol) {

        Stock stock = stockRegistry.getStock(symbol);
        if (stock == null) {
            log.warn("Cancel failed: stock not found symbol={}", symbol);
            return false;
        }

        Order cancelledOrder =
                stock.getOrderBook().getOrderById(orderId);

        if (cancelledOrder == null) {
            log.warn("Cancel failed: order not found id={}", orderId);
            return false;
        }

        boolean cancelled = stock.getOrderBook().cancelOrder(orderId);

        if (cancelled) {
            log.info("Order cancelled: id={}", orderId);
            eventBus.publish(new OrderCancelledEvent(cancelledOrder));
        }

        return cancelled;
    }

    /* ===================== MATCHING ===================== */

    /**
     * Match orders for a specific stock symbol
     */
    public List<Trade> match(String symbol) {

        if (matchingStrategy == null) {
            throw new IllegalStateException("MatchingStrategy not set");
        }

        Stock stock = stockRegistry.getStock(symbol);
        if (stock == null) {
            return List.of();
        }

        List<Trade> trades = matchingStrategy.match(stock.getOrderBook());

        for (Trade trade : trades) {

            log.info(
                    "Trade executed: symbol={}, qty={}, price={}, buyer={}, seller={}",
                    trade.getSymbol(),
                    trade.getQuantity(),
                    trade.getPrice(),
                    trade.getBuyerId(),
                    trade.getSellerId()
            );

            eventBus.publish(new TradeExecutedEvent(trade));
        }

        return trades;
    }

    /* ===================== READ MODELS ===================== */

    public TradeLedger getTradeLedger() {
        return tradeLedger;
    }

    public StockRegistry getStockRegistry() {
        return stockRegistry;
    }
}