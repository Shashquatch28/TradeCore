package com.tradecore.api.dto;

public class MarketOrderRequest {

    public String symbol;
    public int quantity;
    public String side;
    public String traderId;

    public MarketOrderRequest() {}
}