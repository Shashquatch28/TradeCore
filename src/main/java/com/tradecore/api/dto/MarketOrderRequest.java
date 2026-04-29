package com.tradecore.api.dto;

import com.tradecore.enums.OrderSide;

public class MarketOrderRequest {

    public String symbol;
    public int quantity;
    public OrderSide side;
    public String traderId;
}