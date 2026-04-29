package com.tradecore.exception;

public class InsufficientHoldingsException extends RuntimeException {

    public InsufficientHoldingsException(String traderId, String symbol) {
        super("Insufficient holdings of " + symbol + " for trader: " + traderId);
    }
}