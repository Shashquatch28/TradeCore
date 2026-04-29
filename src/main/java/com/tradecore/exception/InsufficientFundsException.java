package com.tradecore.exception;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String traderId) {
        super("Insufficient funds for trader: " + traderId);
    }
}