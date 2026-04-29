package com.tradecore.persistence;

import com.tradecore.events.EventBus;
import com.tradecore.events.TradeExecutedEvent;
import com.tradecore.model.Trade;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TradePersistenceListener {

    private final TradeRepository repository;
    private final EventBus eventBus;

    public TradePersistenceListener(TradeRepository repository,
                                    EventBus eventBus) {
        this.repository = repository;
        this.eventBus = eventBus;
    }

    //  This is the missing piece — registers the listener
    @PostConstruct
    public void init() {
        eventBus.subscribe(TradeExecutedEvent.class, this::onTradeExecuted);
    }

    //  This gets called when a trade is executed
    public void onTradeExecuted(TradeExecutedEvent event) {

        System.out.println("✅ Listener triggered");

        Trade t = event.getTrade();

        TradeEntity entity = new TradeEntity(
                t.getSymbol(),
                t.getPrice(),
                t.getQuantity(),
                t.getBuyerId(),
                t.getSellerId()
        );

        repository.save(entity);

        // Optional but VERY useful for debugging/demo
        System.out.println("✅ Trade persisted: " + t.getSymbol() +
                " | Buyer=" + t.getBuyerId() +
                " | Seller=" + t.getSellerId());
    }
}