package com.tradecore.websocket;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WsMessage<T> {

    private final String type;
    private final T payload;
    private final long timestamp;

    public WsMessage(String type, T payload) {
        this.type = type;
        this.payload = payload;
        this.timestamp = System.currentTimeMillis();
    }

    public String getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }

    public long getTimestamp() {
        return timestamp;
    }
}