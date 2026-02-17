package com.tradecore.observer;

import com.tradecore.model.Trade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TradeLedger {

    private final List<Trade> trades = new ArrayList<>();

    public void record(Trade trade) {
        trades.add(trade);
    }

    public List<Trade> getAllTrades() {
        return Collections.unmodifiableList(trades);
    }
}