package com.tradecore.engine;

import com.tradecore.model.Order;
import com.tradecore.model.Stock;
import com.tradecore.model.Trade;
import com.tradecore.observer.TradeLedger;
import com.tradecore.observer.TradeNotifier;
import com.tradecore.strategy.MatchingStrategy;

import java.util.ArrayList;
import java.util.List;

public class MatchingEngine {

    private static final MatchingEngine INSTANCE = new MatchingEngine();

    private final StockRegistry stockRegistry;
    private MatchingStrategy matchingStrategy;

    private final TradeLedger tradeLedger = new TradeLedger();
    private final List<TradeNotifier> observers = new ArrayList<>();

    private MatchingEngine() {
        this.stockRegistry = new StockRegistry();
    }

    public static MatchingEngine getInstance() {
        return INSTANCE;
    }

    /* ===================== CONFIG ===================== */

    public void setMatchingStrategy(MatchingStrategy strategy) {
        this.matchingStrategy = strategy;
    }

    public void registerObserver(TradeNotifier observer) {
        observers.add(observer);
    }

    /* ===================== ORDER FLOW ===================== */

    public void submitOrder(Order order) {
        Stock stock = stockRegistry.getOrCreateStock(
                order.getSymbol(),
                order.getPrice()
        );
        stock.getOrderBook().addOrder(order);
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
            tradeLedger.record(trade);
            notifyObservers(trade);
        }

        return trades;
    }

    /* ===================== OBSERVERS ===================== */

    private void notifyObservers(Trade trade) {
        for (TradeNotifier observer : observers) {
            observer.onTradeExecuted(trade);
        }
    }

    /* ===================== READ MODELS ===================== */

    public TradeLedger getTradeLedger() {
        return tradeLedger;
    }

    public StockRegistry getStockRegistry() {
        return stockRegistry;
    }
}