package com.tradecore.engine;

import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderType;
import com.tradecore.model.*;
import com.tradecore.trader.Trader;

public class OrderFactory {

    private OrderFactory() {
        // Prevent instantiation
    }

    public static Order createOrder(
            OrderType type,
            String orderId,
            String symbol,
            int quantity,
            OrderSide side,
            Double price,
            Trader trader
    ) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("Symbol cannot be null or empty");
        }

        if (trader == null) {
            throw new IllegalArgumentException("Trader cannot be null");
        }

        return switch (type) {

            case MARKET -> new MarketOrder(
                    orderId,
                    symbol,
                    quantity,
                    side,
                    trader
            );

            case LIMIT -> {
                if (price == null || price <= 0) {
                    throw new IllegalArgumentException("Limit price must be positive");
                }
                yield new LimitOrder(
                        orderId,
                        symbol,
                        quantity,
                        side,
                        price,
                        trader
                );
            }

            case STOP_LOSS -> {
                if (price == null || price <= 0) {
                    throw new IllegalArgumentException("Stop price must be positive");
                }
                yield new StopLossOrder(
                        orderId,
                        symbol,
                        quantity,
                        side,
                        price,
                        trader
                );
            }
        };
    }
}