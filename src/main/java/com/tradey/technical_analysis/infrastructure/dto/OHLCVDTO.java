package com.tradey.technical_analysis.infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NonNull;

@Getter
@Builder
public class OHLCVDTO {
    private final @NonNull String symbol;
    private final @NonNull String timestamp;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final double volume;


    @Setter
    private Double ma50;
    @Setter
    private Double ma200;
    @Setter
    private Double diff_ma50_ma200;

    public OHLCVEntity toEntity() {
        return OHLCVEntity.builder()
                .symbol(symbol)
                .timestamp(timestamp)
                .open(open)
                .high(high)
                .low(low)
                .close(close)
                .volume(volume)
                .ma50(ma50)
                .ma200(ma200)
                .diffMa50Ma200(diff_ma50_ma200)
                .build();
    }
}
