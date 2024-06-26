package com.tradey.technical_analysis.infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.Getter;

import static com.tradey.technical_analysis.pkgs.Constants.serviceName;

@RequiredArgsConstructor
@Getter
public class StateMachineDTO {
    private final String service;
    private final String symbol;
    private final Map<String, Object> state;
    private final String updated_at;

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
