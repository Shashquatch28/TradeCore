package com.tradecore.websocket.dto;

public class PriceDto {

    private final String symbol;
    private final double price;

    public PriceDto(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }
}