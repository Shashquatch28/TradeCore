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

        if (orderBook == null) {
            return trades;
        }

        while (orderBook.hasBuyOrders() && orderBook.hasSellOrders()) {

            Order buy = orderBook.getBestBuy();
            Order sell = orderBook.getBestSell();

            // Safety check
            if (buy == null || sell == null) {
                break;
            }

            // No price match possible
            if (buy.getPrice() < sell.getPrice()) {
                break;
            }

            int tradedQty = Math.min(buy.getQuantity(), sell.getQuantity());
            double tradePrice = sell.getPrice(); // price-time priority

            Trade trade = new Trade(
                    buy.getSymbol(),
                    tradePrice,
                    tradedQty,
                    buy.getTrader().getTraderId(),
                    sell.getTrader().getTraderId()
            );

            trades.add(trade);

            // Reduce quantities (supports partial fills)
            buy.reduceQuantity(tradedQty);
            sell.reduceQuantity(tradedQty);

            // Remove fully filled orders
            if (buy.getQuantity() == 0) {
                orderBook.removeBestBuy();
            }

            if (sell.getQuantity() == 0) {
                orderBook.removeBestSell();
            }
        }

        return trades;
    }
}