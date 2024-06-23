package com.tradey.technical_analysis.domain.repositories;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;

public interface StateMachineRepository {
    /***
     * Retrieve data from state machine table
     */
    TAStateMachineEntity getTA(String symbol);
    void upsertTA(String symbol, TAStateMachineEntity entity);
}
