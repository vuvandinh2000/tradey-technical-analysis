package com.tradey.technical_analysis.domain.services;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.repositories.StateMachineRepository;

public class StateMachineService {
    private final StateMachineRepository stateMachineRepository;

    public StateMachineService(StateMachineRepository stateMachineRepository) {
        this.stateMachineRepository = stateMachineRepository;
    }

    public TAStateMachineEntity getTA(String symbol) {
        return stateMachineRepository.getTA(symbol);
    }

    public void upsertTA(String symbol, TAStateMachineEntity entity) {
        stateMachineRepository.upsertTA(symbol, entity);
    }
}
