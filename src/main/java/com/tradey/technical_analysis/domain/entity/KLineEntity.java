package com.tradey.technical_analysis.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
public class KLineEntity {
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    private long openTime;
    private long closeTime;

    private Double quoteAssetVolume;
    private int numberOfTrades;
    private Double takerBuyBaseAssetVolume;
    private Double takerBuyQuoteAssetVolume;

    private Double ma7;
    private Double ma25;
    private Double ma50;
    private Double ma99;
    private Double ma200;
    private Double diffMa25Ma99;
    private Double diffMa50Ma200;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("open", String.format("%.8f", open));
        map.put("high", String.format("%.8f", high));
        map.put("low", String.format("%.8f", low));
        map.put("close", String.format("%.8f", close));
        map.put("volume", String.format("%.8f", volume));
        map.put("openTime", openTime);
        map.put("closeTime", closeTime);
        map.put("quoteAssetVolume", quoteAssetVolume != null ? String.format("%.8f", quoteAssetVolume) : null);
        map.put("numberOfTrades", numberOfTrades);
        map.put("takerBuyBaseAssetVolume", takerBuyBaseAssetVolume != null ? String.format("%.8f", takerBuyBaseAssetVolume) : null);
        map.put("takerBuyQuoteAssetVolume", takerBuyQuoteAssetVolume != null ? String.format("%.8f", takerBuyQuoteAssetVolume) : null);
        map.put("ma7", ma7 != null ? String.format("%.8f", ma7) : null);
        map.put("ma25", ma25 != null ? String.format("%.8f", ma25) : null);
        map.put("ma50", ma50 != null ? String.format("%.8f", ma50) : null);
        map.put("ma99", ma99 != null ? String.format("%.8f", ma99) : null);
        map.put("ma200", ma200 != null ? String.format("%.8f", ma200) : null);
        map.put("diffMa25Ma99", diffMa25Ma99 != null ? String.format("%.8f", diffMa25Ma99) : null);
        map.put("diffMa50Ma200", diffMa50Ma200 != null ? String.format("%.8f", diffMa50Ma200) : null);
        return map;
    }
}
