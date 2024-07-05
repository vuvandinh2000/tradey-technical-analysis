package com.tradey.technical_analysis.infrastructure.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Getter
public class TechnicalAnalysisEventDTO {
    private final String exchangeType;
    private final String symbol;
    private final String timestamp;
    private final boolean force;


    public static TechnicalAnalysisEventDTO fromEventMap(Map<String, Object> event) {
        String exchangeType = (String) event.get("exchange_type");
        String symbol = (String) event.get("symbol");
        String timestamp = (String) event.get("timestamp");
        boolean force = (Boolean) event.getOrDefault("force", false);

        if (exchangeType == null) {
            exchangeType = "FUTURES-U_MARGINED";
            log.warn(String.format("Event has 'exchange_type' is null, use default='%s'", exchangeType));
        }

        return new TechnicalAnalysisEventDTO(exchangeType, symbol, timestamp, force);

    }
}
