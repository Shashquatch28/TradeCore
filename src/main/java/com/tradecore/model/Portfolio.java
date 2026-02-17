package com.tradecore.model;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    private double cashBalance;
    private final Map<String, Integer> positions = new HashMap<>();

    public Portfolio(double initialCash) {
        this.cashBalance = initialCash;
    }

    public void addPosition(String symbol, int quantity, double price) {
        positions.put(symbol, positions.getOrDefault(symbol, 0) + quantity);
        cashBalance -= quantity * price;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public Map<String, Integer> getPositions() {
        return Map.copyOf(positions);
    }
}