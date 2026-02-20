package com.tradecore.marketdata;

public interface MarketDataProvider {

    MarketPrice fetchPrice(String symbol) throws Exception;
}