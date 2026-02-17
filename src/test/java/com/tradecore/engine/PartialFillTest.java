package com.tradecore.engine;

import com.tradecore.engine.OrderFactory;
import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderType;
import com.tradecore.model.Order;
import com.tradecore.strategy.FIFOMatchingStrategy;
import com.tradecore.trader.RetailTrader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PartialFillTest {

    @Test
    void largeOrder_isPartiallyFilled() {

        MatchingEngine engine = MatchingEngine.getInstance();
        engine.setMatchingStrategy(new FIFOMatchingStrategy());

        RetailTrader buyer = new RetailTrader("T1", "Alice", 100_000);
        RetailTrader seller = new RetailTrader("T2", "Bob", 100_000);

        Order buy = OrderFactory.createOrder(
                OrderType.LIMIT, "B1", "AAPL", 100, OrderSide.BUY, 150.0, buyer
        );

        Order sell = OrderFactory.createOrder(
                OrderType.LIMIT, "S1", "AAPL", 40, OrderSide.SELL, 149.0, seller
        );

        engine.submitOrder(buy);
        engine.submitOrder(sell);

        engine.match("AAPL");

        assertEquals(60, buy.getQuantity());
        assertEquals(0, sell.getQuantity());
    }
}