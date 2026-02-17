package com.tradecore.engine;

import com.tradecore.model.Stock;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StockRegistry {

    private final Map<String, Stock> stocks = new ConcurrentHashMap<>();

    public Stock getOrCreateStock(String symbol, double initialPrice) {
        return stocks.computeIfAbsent(
                symbol,
                s -> new Stock(s, initialPrice)
        );
    }

    public Stock getStock(String symbol) {
        return stocks.get(symbol);
    }

    public Map<String, Stock> getAllStocks() {
        return stocks;
    }
}