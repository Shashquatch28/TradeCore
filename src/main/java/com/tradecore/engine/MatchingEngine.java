package com.tradecore.engine;

import com.tradecore.events.*;
import com.tradecore.model.*;
import com.tradecore.observer.TradeLedger;
import com.tradecore.strategy.MatchingStrategy;
import com.tradecore.metrics.EventMetricsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {

    private static final Logger log =
            LoggerFactory.getLogger(MatchingEngine.class);

    private final StockRegistry stockRegistry;
    private MatchingStrategy matchingStrategy;

    private final EventBus eventBus = new EventBus();
    private final TradeLedger tradeLedger = new TradeLedger();
    private final EventMetricsListener metrics = new EventMetricsListener();
    private final PriceUpdateListener priceUpdateListener;

    // 🔥 NEW: Stop-loss storage
    private final List<StopLossOrder> stopOrders = new ArrayList<>();

    public MatchingEngine() {
        this.stockRegistry = new StockRegistry();
        this.priceUpdateListener = new PriceUpdateListener(stockRegistry);

        eventBus.subscribe(TradeExecutedEvent.class, tradeLedger::onTradeExecuted);
        eventBus.subscribe(TradeExecutedEvent.class, metrics::onTradeExecuted);
        eventBus.subscribe(PriceTickEvent.class, priceUpdateListener::onPriceTick);
        eventBus.subscribe(PriceTickEvent.class,
                new AutoMatchOnPriceListener(this)::onPriceTick);

        // 🔥 NEW: Stop-loss trigger listener
        eventBus.subscribe(PriceTickEvent.class, this::handleStopLoss);
    }

    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    /* ===================== ORDER FLOW ===================== */

    public void submitOrder(Order order) {

        // 🔥 Minimal validation
        if (order.getQuantity() <= 0) {
            log.warn("Invalid order rejected: {}", order.getOrderId());
            return;
        }

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

        order.process(this, stock);
    }

    /* ===================== STOP LOSS ===================== */

    public void registerStopLossOrder(StopLossOrder order) {
        stopOrders.add(order);
    }

    private void handleStopLoss(PriceTickEvent event) {

        double currentPrice = event.getPrice();
        String symbol = event.getSymbol();

        List<StopLossOrder> triggered = new ArrayList<>();

        for (StopLossOrder order : stopOrders) {

            if (!order.getSymbol().equals(symbol)) continue;

            boolean shouldTrigger =
                    (order.getSide() == com.tradecore.enums.OrderSide.SELL && currentPrice <= order.getStopPrice()) ||
                    (order.getSide() == com.tradecore.enums.OrderSide.BUY && currentPrice >= order.getStopPrice());

            if (shouldTrigger) {
                triggered.add(order);
            }
        }

        for (StopLossOrder order : triggered) {

            stopOrders.remove(order);

            log.info("Stop-loss triggered: {}", order.getOrderId());

            MarketOrder marketOrder = new MarketOrder(
                    order.getOrderId(),
                    order.getSymbol(),
                    order.getQuantity(),
                    order.getSide(),
                    order.getTrader()
            );

            submitOrder(marketOrder);
        }
    }

    /* ===================== MARKET ORDER LOGIC ===================== */

    public void processMarketOrder(Order order, Stock stock) {

        var orderBook = stock.getOrderBook();

        while (order.getQuantity() > 0) {

            Order bestOpposite =
                    order.getSide() == com.tradecore.enums.OrderSide.BUY
                            ? orderBook.getBestSell()
                            : orderBook.getBestBuy();

            if (bestOpposite == null) {
                log.warn("Market order cannot be filled: no liquidity symbol={}", order.getSymbol());
                return;
            }

            int tradeQty = Math.min(order.getQuantity(), bestOpposite.getQuantity());
            double tradePrice = bestOpposite.getPrice();

            String buyerId;
            String sellerId;

            if (order.getSide() == com.tradecore.enums.OrderSide.BUY) {
                buyerId = order.getTrader().getTraderId();
                sellerId = bestOpposite.getTrader().getTraderId();
            } else {
                buyerId = bestOpposite.getTrader().getTraderId();
                sellerId = order.getTrader().getTraderId();
            }

            Trade trade = new Trade(
                    order.getSymbol(),
                    tradePrice,
                    tradeQty,
                    buyerId,
                    sellerId
            );

            order.reduceQuantity(tradeQty);
            bestOpposite.reduceQuantity(tradeQty);

            if (bestOpposite.getQuantity() == 0) {
                if (bestOpposite.getSide() == com.tradecore.enums.OrderSide.BUY) {
                    orderBook.removeBestBuy();
                } else {
                    orderBook.removeBestSell();
                }
            }

            log.info("Market trade executed: {}", trade);
            eventBus.publish(new TradeExecutedEvent(trade));
        }
    }

    /* ===================== MATCHING ===================== */

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

    public void matchIfPossible(String symbol) {
        try {
            List<Trade> trades = match(symbol);

            if (!trades.isEmpty()) {
                log.info(
                        "Auto-matching triggered for symbol={} trades={}",
                        symbol,
                        trades.size()
                );
            }
        } catch (Exception e) {
            log.error("Auto-matching failed for symbol={}", symbol, e);
        }
    }

    public TradeLedger getTradeLedger() {
        return tradeLedger;
    }

    public StockRegistry getStockRegistry() {
        return stockRegistry;
    }

    public EventMetricsListener getMetrics() {
        return metrics;
    }
}