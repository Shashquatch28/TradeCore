package com.tradecore.api;

import com.tradecore.persistence.TradeEntity;
import com.tradecore.service.TradeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trades")
public class TradeController {

    private final TradeService service;

    public TradeController(TradeService service) {
        this.service = service;
    }

    // READ ALL
    @GetMapping
    public List<TradeEntity> getAllTrades() {
        return service.getAllTrades();
    }

    // READ ONE
    @GetMapping("/{id}")
    public TradeEntity getTrade(@PathVariable Long id) {
        return service.getTrade(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public TradeEntity updateTrade(@PathVariable Long id,
                                   @RequestBody TradeEntity updated) {
        return service.updateTrade(id, updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteTrade(@PathVariable Long id) {
        service.deleteTrade(id);
        return "Trade deleted";
    }
}