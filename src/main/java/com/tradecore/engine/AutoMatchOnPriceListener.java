package com.tradecore.engine;

import com.tradecore.events.PriceTickEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Triggers matching whenever a price update occurs.
 */
public class AutoMatchOnPriceListener {

    private static final Logger log =
            LoggerFactory.getLogger(AutoMatchOnPriceListener.class);

    private final MatchingEngine engine;

    public AutoMatchOnPriceListener(MatchingEngine engine) {
        this.engine = engine;
    }

    public void onPriceTick(PriceTickEvent event) {

        log.debug(
                "Auto-match check triggered by price tick: symbol={} price={}",
                event.getSymbol(),
                event.getPrice()
        );

        engine.matchIfPossible(event.getSymbol());
    }
}