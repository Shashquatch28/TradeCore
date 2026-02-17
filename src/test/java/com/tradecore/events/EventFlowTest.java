package com.tradecore.events;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.engine.OrderFactory;
import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderType;
import com.tradecore.model.Order;
import com.tradecore.observer.TradeLedger;
import com.tradecore.strategy.FIFOMatchingStrategy;
import com.tradecore.trader.RetailTrader;
import com.tradecore.trader.InstitutionalTrader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EventFlowTest {

    @Test
    void tradeExecutedEvent_updatesTradeLedger() {

        MatchingEngine engine = MatchingEngine.getInstance();
        engine.setMatchingStrategy(new FIFOMatchingStrategy());

        TradeLedger ledger = engine.getTradeLedger();
        ledger.clear();

        RetailTrader buyer = new RetailTrader("T1", "Alice", 100_000);
        InstitutionalTrader seller = new InstitutionalTrader("T2", "Bob", 100_000);

        Order buy = OrderFactory.createOrder(
                OrderType.LIMIT, "O1", "AAPL", 10, OrderSide.BUY, 150.0, buyer
        );

        Order sell = OrderFactory.createOrder(
                OrderType.LIMIT, "O2", "AAPL", 10, OrderSide.SELL, 149.0, seller
        );

        engine.submitOrder(buy);
        engine.submitOrder(sell);
        engine.match("AAPL");

        assertEquals(1, ledger.getAllTrades().size());
    }
}