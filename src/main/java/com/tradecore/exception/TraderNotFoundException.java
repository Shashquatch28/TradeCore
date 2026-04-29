package com.tradecore.exception;

public class TraderNotFoundException extends RuntimeException {

    public TraderNotFoundException(String traderId) {
        super("Trader not found: " + traderId);
    }
}