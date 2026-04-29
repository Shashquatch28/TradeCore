package com.tradecore.service;

import com.tradecore.persistence.TradeEntity;
import com.tradecore.persistence.TradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService {

    private final TradeRepository repository;

    public TradeService(TradeRepository repository) {
        this.repository = repository;
    }

    public List<TradeEntity> getAllTrades() {
        return repository.findAll();
    }

    public TradeEntity getTrade(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public TradeEntity updateTrade(Long id, TradeEntity updated) {
        TradeEntity existing = repository.findById(id).orElseThrow();

        existing.setPrice(updated.getPrice());
        existing.setQuantity(updated.getQuantity());

        return repository.save(existing);
    }

    public void deleteTrade(Long id) {
        repository.deleteById(id);
    }
}