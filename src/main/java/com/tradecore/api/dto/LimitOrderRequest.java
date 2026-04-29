package com.tradecore.api.dto;

public class LimitOrderRequest {

    public String symbol;
    public int quantity;
    public String side;
    public double price;
    public String traderId;

    public LimitOrderRequest() {}
}