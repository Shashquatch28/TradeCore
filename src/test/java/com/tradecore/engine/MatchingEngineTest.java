package com.tradecore.engine;

import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderType;
import com.tradecore.model.Order;
import com.tradecore.model.Trade;
import com.tradecore.strategy.FIFOMatchingStrategy;
import com.tradecore.trader.RetailTrader;
import com.tradecore.trader.InstitutionalTrader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingEngineTest {

    private MatchingEngine engine;
    private RetailTrader buyer;
    private InstitutionalTrader seller;

    @BeforeEach
    void setup() {
        engine = MatchingEngine.getInstance();
        engine.setMatchingStrategy(new FIFOMatchingStrategy());

        buyer = new RetailTrader("B1", "Buyer", 100_000);
        seller = new InstitutionalTrader("S1", "Seller", 100_000);
    }

    @Test
    void testExactMatch() {

        Order buyOrder = OrderFactory.createOrder(
                OrderType.LIMIT,
                "O1",
                "AAPL",
                100,
                OrderSide.BUY,
                150.0,
                buyer
        );

        Order sellOrder = OrderFactory.createOrder(
                OrderType.LIMIT,
                "O2",
                "AAPL",
                100,
                OrderSide.SELL,
                150.0,
                seller
        );

        engine.submitOrder(buyOrder);
        engine.submitOrder(sellOrder);

        List<Trade> trades = engine.match("AAPL");

        assertEquals(1, trades.size());

        Trade trade = trades.get(0);
        assertEquals("AAPL", trade.getSymbol());
        assertEquals(100, trade.getQuantity());
        assertEquals(150.0, trade.getPrice());
        assertEquals("B1", trade.getBuyerId());
        assertEquals("S1", trade.getSellerId());
    }

    @Test
    void testPartialFill() {

        Order buyOrder = OrderFactory.createOrder(
                OrderType.LIMIT,
                "O3",
                "TSLA",
                200,
                OrderSide.BUY,
                300.0,
                buyer
        );

        Order sellOrder = OrderFactory.createOrder(
                OrderType.LIMIT,
                "O4",
                "TSLA",
                100,
                OrderSide.SELL,
                300.0,
                seller
        );

        engine.submitOrder(buyOrder);
        engine.submitOrder(sellOrder);

        List<Trade> trades = engine.match("TSLA");

        assertEquals(1, trades.size());
        assertEquals(100, trades.get(0).getQuantity());

        assertEquals(100, buyOrder.getQuantity()); // remaining
        assertEquals(0, sellOrder.getQuantity());  // filled
    }
}