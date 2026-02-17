package com.tradecore;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.engine.OrderFactory;
import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderType;
import com.tradecore.model.Order;
import com.tradecore.model.Trade;
import com.tradecore.strategy.FIFOMatchingStrategy;
import com.tradecore.trader.InstitutionalTrader;
import com.tradecore.trader.RetailTrader;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Traders
        RetailTrader alice = new RetailTrader("T1", "Alice", 100_000);
        InstitutionalTrader bob = new InstitutionalTrader("T2", "Bob Fund", 100_000);

        // 2. Engine + strategy
        MatchingEngine engine = MatchingEngine.getInstance();
        engine.setMatchingStrategy(new FIFOMatchingStrategy());

        engine.registerObserver(alice);
        engine.registerObserver(bob);

        // 3. Create orders via OrderFactory
        Order buyOrder = OrderFactory.createOrder(
                OrderType.LIMIT,
                "O1",
                "AAPL",
                100,
                OrderSide.BUY,
                150.0,
                alice
        );

        Order sellOrder = OrderFactory.createOrder(
                OrderType.LIMIT,
                "O2",
                "AAPL",
                100,
                OrderSide.SELL,
                149.0,
                bob
        );

        engine.submitOrder(buyOrder);
        engine.submitOrder(sellOrder);

        // 4. Match for a specific symbol
        List<Trade> trades = engine.match("AAPL");

        // 5. Output
        System.out.println("=== Trades Executed ===");
        trades.forEach(trade ->
                System.out.println(
                        trade.getSymbol() +
                                " | Qty: " + trade.getQuantity() +
                                " | Price: " + trade.getPrice() +
                                " | Buyer: " + trade.getBuyerId() +
                                " | Seller: " + trade.getSellerId()
                )
        );

        System.out.println("\n=== Portfolios ===");
        System.out.println("Alice cash: " + alice.getPortfolio().getCashBalance());
        System.out.println("Alice positions: " + alice.getPortfolio().getPositions());

        System.out.println("Bob cash: " + bob.getPortfolio().getCashBalance());
        System.out.println("Bob positions: " + bob.getPortfolio().getPositions());
    }
}