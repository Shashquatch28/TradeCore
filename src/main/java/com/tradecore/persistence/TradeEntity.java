package com.tradecore.persistence;

import jakarta.persistence.*;

@Entity
public class TradeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String symbol;
    private double price;
    private int quantity;
    private String buyerId;
    private String sellerId;

    public TradeEntity() {}

    public TradeEntity(String symbol, double price, int quantity,
                       String buyerId, String sellerId) {
        this.symbol = symbol;
        this.price = price;
        this.quantity = quantity;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
    }

    public Long getId() { return id; }
    public String getSymbol() { return symbol; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public String getBuyerId() { return buyerId; }
    public String getSellerId() { return sellerId; }

    public void setSymbol(String symbol) { this.symbol = symbol; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setBuyerId(String buyerId) { this.buyerId = buyerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }
}