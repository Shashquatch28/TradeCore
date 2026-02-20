package com.tradecore.websocket.dto;

public class TradeDto {

    private final String symbol;
    private final int quantity;
    private final double price;
    private final String buyerId;
    private final String sellerId;

    public TradeDto(
            String symbol,
            int quantity,
            double price,
            String buyerId,
            String sellerId
    ) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }
}