package com.tradey.technical_analysis.infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.tradey.technical_analysis.pkgs.Constants.serviceName;

public class StateMachineDTO {
    private String service;
    private String symbol;
    private Map<String, Object> state;
    private String updated_at;

    public StateMachineDTO(String service, String symbol, Map<String, Object> state, String updated_at) {
        this.service = service;
        this.symbol = symbol;
        this.state = state;
        this.updated_at = updated_at;
    }

    public String getService() {
        return service;
    }

    public String getSymbol() {
        return symbol;
    }

    public Map<String, Object> getState() {
        return state;
    }

    public String getUpdatedAt() {
        return updated_at;
    }

    public TAStateMachineEntity toEntity() {
        if (!state.containsKey("latest_timestamp_processed")) {
            throw new NoSuchElementException("ValuationError: latest_timestamp_processed not found in state");
        }

        String latestTimestampProcessed = (String) state.get("latest_timestamp_processed");
        return new TAStateMachineEntity(symbol, updated_at, latestTimestampProcessed);
    }

    public static StateMachineDTO fromEntity(TAStateMachineEntity entity) {
        Map<String, Object> state = new HashMap<>();
        state.put("latest_timestamp_processed", entity.getLatestTimestampProcessed());
        return new StateMachineDTO(serviceName, entity.getSymbol(), state, entity.getUpdatedAt());
    }
}
