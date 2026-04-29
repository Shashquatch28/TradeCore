package com.tradecore.api.dto;

import com.tradecore.enums.OrderSide;

public class LimitOrderRequest {

    public String symbol;
    public int quantity;
    public double price;
    public OrderSide side;
    public String traderId;

}