package com.tradey.technical_analysis.domain.services;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.repositories.StateMachineRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StateMachineService {
    private final StateMachineRepository stateMachineRepository;

    public TAStateMachineEntity getTA(String symbol) {
        return stateMachineRepository.getTA(symbol);
    }

    public void upsertTA(String symbol, TAStateMachineEntity entity) {
        stateMachineRepository.upsertTA(symbol, entity);
    }
}
