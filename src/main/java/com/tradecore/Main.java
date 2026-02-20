package com.tradecore;

import com.tradecore.engine.MatchingEngine;
import com.tradecore.engine.OrderFactory;
import com.tradecore.enums.OrderSide;
import com.tradecore.enums.OrderType;
import com.tradecore.events.PriceTickEvent;
import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.marketdata.FinnhubRestClient;
import com.tradecore.marketdata.MarketDataProvider;
import com.tradecore.marketdata.MarketDataService;
import com.tradecore.model.Order;
import com.tradecore.portfolio.PortfolioUpdater;
import com.tradecore.registry.TraderRegistry;
import com.tradecore.strategy.FIFOMatchingStrategy;
import com.tradecore.trader.InstitutionalTrader;
import com.tradecore.trader.RetailTrader;
import com.tradecore.websocket.WebSocketServer;

import java.time.Duration;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        /* ===================== ENGINE ===================== */

        MatchingEngine engine = MatchingEngine.getInstance();
        engine.setMatchingStrategy(new FIFOMatchingStrategy());

        /* ===================== WEBSOCKET SERVER ===================== */

        WebSocketServer wsServer = new WebSocketServer();
        wsServer.start();

        engine.getEventBus().subscribe(
                PriceTickEvent.class,
                wsServer::handlePriceTick
        );

        engine.getEventBus().subscribe(
                TradeExecutedEvent.class,
                wsServer::handleTradeExecuted
        );

        /* ===================== TRADERS ===================== */

        RetailTrader alice = new RetailTrader("T1", "Alice", 100_000);
        InstitutionalTrader bob = new InstitutionalTrader("T2", "Bob Fund", 100_000);

        /* ===================== MARKET DATA ===================== */

        MarketDataProvider provider = new FinnhubRestClient();
        MarketDataService marketDataService =
                new MarketDataService(provider, engine.getEventBus());

        marketDataService.start(
                List.of("AAPL"),
                Duration.ofSeconds(10)
        );

        /* ===================== REGISTRIES ===================== */

        TraderRegistry traderRegistry = new TraderRegistry();
        traderRegistry.registerTrader(alice);
        traderRegistry.registerTrader(bob);

        PortfolioUpdater portfolioUpdater =
                new PortfolioUpdater(traderRegistry);

        engine.getEventBus().subscribe(
                TradeExecutedEvent.class,
                portfolioUpdater::onTradeExecuted
        );

        /* ===================== ORDERS ===================== */

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

        /* ===================== PORTFOLIOS ===================== */

        System.out.println("\n=== Portfolios ===");
        System.out.println("Alice cash: " + alice.getPortfolio().getCashBalance());
        System.out.println("Alice positions: " + alice.getPortfolio().getPositions());
        System.out.println("Bob cash: " + bob.getPortfolio().getCashBalance());
        System.out.println("Bob positions: " + bob.getPortfolio().getPositions());

        /* ===================== KEEP ALIVE ===================== */

        Thread.sleep(60_000);

        /* ===================== SHUTDOWN ===================== */

        marketDataService.stop();
        wsServer.stop();
    }
}