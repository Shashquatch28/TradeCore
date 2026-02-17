package com.tradecore.strategy;

import com.tradecore.engine.OrderBook;
import com.tradecore.model.Order;
import com.tradecore.model.Trade;

import java.util.ArrayList;
import java.util.List;

public class FIFOMatchingStrategy implements MatchingStrategy {

    @Override
    public List<Trade> match(OrderBook orderBook) {

        List<Trade> trades = new ArrayList<>();

        while (orderBook.hasBuyOrders() && orderBook.hasSellOrders()) {

            Order buy = orderBook.getBestBuy();
            Order sell = orderBook.getBestSell();

            if (buy.getPrice() < sell.getPrice()) {
                break; // No price match possible
            }

            int tradedQty = Math.min(buy.getQuantity(), sell.getQuantity());
            double tradePrice = sell.getPrice();

            Trade trade = new Trade(
                    buy.getSymbol(),
                    tradePrice,
                    tradedQty
            );

            trades.add(trade);

            // NOTE: quantity reduction & order removal
            // will be handled by MatchingEngine (next step)
            break;
        }

        return trades;
    }
}