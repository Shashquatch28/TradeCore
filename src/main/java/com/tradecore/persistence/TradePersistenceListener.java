package com.tradecore.persistence;

import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Trade;
import org.springframework.stereotype.Component;

@Component
public class TradePersistenceListener {

    private final TradeRepository repository;

    public TradePersistenceListener(TradeRepository repository) {
        this.repository = repository;
    }

    public void onTradeExecuted(TradeExecutedEvent event) {

        Trade t = event.getTrade();

        TradeEntity entity = new TradeEntity(
                t.getSymbol(),
                t.getPrice(),
                t.getQuantity(),
                t.getBuyerId(),
                t.getSellerId()
        );

        repository.save(entity);
    }
}