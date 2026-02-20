package com.tradecore.engine;

import com.tradecore.events.PriceTickEvent;
import com.tradecore.model.Stock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PriceUpdateListener {

    private static final Logger log =
            LoggerFactory.getLogger(PriceUpdateListener.class);

    private final StockRegistry stockRegistry;

    public PriceUpdateListener(StockRegistry stockRegistry) {
        this.stockRegistry = stockRegistry;
    }

    public void onPriceTick(PriceTickEvent event) {

        Stock stock = stockRegistry.getStock(event.getSymbol());
        if (stock == null) {
            return;
        }

        stock.updatePrice(event.getPrice());

        log.info(
                "Stock price updated: symbol={}, price={}",
                event.getSymbol(),
                event.getPrice()
        );
    }
}