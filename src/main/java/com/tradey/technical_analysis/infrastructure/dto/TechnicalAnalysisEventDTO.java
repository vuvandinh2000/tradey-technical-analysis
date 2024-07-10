package com.tradey.technical_analysis.infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.KLineEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
@Getter
public class TechnicalAnalysisEventDTO {
    private final List<KLineEntity> latestKLines;
    private final Map<String, Object> properties;


    public static TechnicalAnalysisEventDTO fromEventMap(Map<String, Object> event) {
        List<Map<String, Object>> kLinesMap = (List<Map<String, Object>>) event.get("latestKLines");
        Map<String, Object> properties = (HashMap<String, Object>) event.get("properties");

        List<KLineEntity> latestKLines = new ArrayList<>();
        for (Map<String, Object> kLineMap: kLinesMap) {
            latestKLines.add(
                    KLineEntity.builder()
                            .open(Double.parseDouble((String) kLineMap.get("open")))
                            .high(Double.parseDouble((String) kLineMap.get("high")))
                            .low(Double.parseDouble((String) kLineMap.get("low")))
                            .close(Double.parseDouble((String) kLineMap.get("close")))
                            .volume(Double.parseDouble((String) kLineMap.get("volume")))
                            .openTime((Long) kLineMap.get("openTime"))
                            .closeTime((Long) kLineMap.get("closeTime"))
                            .quoteAssetVolume(Double.parseDouble((String) kLineMap.get("quoteAssetVolume")))
                            .numberOfTrades((int) kLineMap.get("numberOfTrades"))
                            .takerBuyBaseAssetVolume(Double.parseDouble((String) kLineMap.get("takerBuyBaseAssetVolume")))
                            .takerBuyQuoteAssetVolume(Double.parseDouble((String) kLineMap.get("takerBuyQuoteAssetVolume")))
                            .build()
            );
        }

        return new TechnicalAnalysisEventDTO(latestKLines, properties);
    }
}
