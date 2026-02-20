package com.tradecore.websocket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/market")
public class MarketWebSocketEndpoint {

    private static final Set<Session> sessions =
            ConcurrentHashMap.newKeySet();

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        sessions.remove(session);
    }

    public static void broadcast(String message) {
        for (Session session : sessions) {
            if (session.isOpen()) {
                session.getAsyncRemote().sendText(message);
            }
        }
    }
}