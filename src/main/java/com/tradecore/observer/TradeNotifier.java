package com.tradecore.observer;

import com.tradecore.model.Trade;

public interface TradeNotifier {
    void onTradeExecuted(Trade trade);
}