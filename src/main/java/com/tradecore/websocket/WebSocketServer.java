package com.tradecore.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradecore.events.PriceTickEvent;
import com.tradecore.events.TradeExecutedEvent;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.glassfish.tyrus.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws/market")
public class WebSocketServer {

    private static final Logger log =
            LoggerFactory.getLogger(WebSocketServer.class);

    private static final Set<Session> sessions =
            new CopyOnWriteArraySet<>();

    private static final ObjectMapper mapper = new ObjectMapper();

    private Server server;

    /* ===================== SERVER LIFECYCLE ===================== */

    public void start() {
        try {
            server = new Server(
                    "localhost",
                    8080,
                    "/",
                    null,
                    WebSocketServer.class
            );
            server.start();
            log.info("WebSocket server started at ws://localhost:8080/ws/market");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start WebSocket server", e);
        }
    }

    public void stop() {
        if (server != null) {
            server.stop();
            log.info("WebSocket server stopped");
        }
    }

    /* ===================== SOCKET EVENTS ===================== */

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        log.info("WebSocket connected: {}", session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        log.info("WebSocket disconnected: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket error session={}", session.getId(), error);
    }

    /* ===================== DOMAIN EVENT HANDLERS ===================== */

    public void handlePriceTick(PriceTickEvent event) {
        broadcast(Map.of(
                "type", "PRICE_TICK",
                "data", event.getPrice()
        ));
    }

    public void handleTradeExecuted(TradeExecutedEvent event) {
        broadcast(Map.of(
                "type", "TRADE_EXECUTED",
                "data", event.getTrade()
        ));
    }

    /* ===================== BROADCAST ===================== */

    private void broadcast(Object payload) {
        try {
            String json = mapper.writeValueAsString(payload);

            for (Session session : sessions) {
                if (session.isOpen()) {
                    session.getAsyncRemote().sendText(json);
                }
            }
        } catch (Exception e) {
            log.error("Failed to broadcast event", e);
        }
    }
}