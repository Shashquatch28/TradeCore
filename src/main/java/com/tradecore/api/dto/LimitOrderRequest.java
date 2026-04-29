package com.tradecore.api.dto;

import com.tradecore.enums.OrderSide;

public class LimitOrderRequest {

    public String symbol;
    public int quantity;
    public OrderSide side;
    public double price;
    public String traderId;
}