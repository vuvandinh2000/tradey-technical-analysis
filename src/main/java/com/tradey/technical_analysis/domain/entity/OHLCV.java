package com.tradey.technical_analysis.domain.entity;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OHLCV {
    private String symbol;
    private String timestamp;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;

    private Double ma50;
    private Double ma200;
    private Double diffMa50Ma200;
}
