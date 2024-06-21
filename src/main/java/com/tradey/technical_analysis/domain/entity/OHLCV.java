package com.tradey.technical_analysis.domain.entity;

import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoOHLCVRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

public class OHLCV {
    private final Logger logger = Logger.getLogger(DynamoOHLCVRepository.class.getName());

    private String symbol;
    private float open;
    private float high;
    private float low;
    private float close;
    private float volume;
    private LocalDateTime timestamp;

    private Optional<Float> ma50;
    private Optional<Float> ma200;
    private Optional<Float> diffMa50Ma200;

    public OHLCV(String symbol, float open, float high, float low, float close, float volume, LocalDateTime timestamp, Optional<Float> ma50, Optional<Float> ma200, Optional<Float> diffMa50Ma200) {
        this.symbol = symbol;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.timestamp = timestamp;
        this.ma50 = ma50;
        this.ma200 = ma200;
        this.diffMa50Ma200 = diffMa50Ma200;
    }

    public void setMa50(float ma50) {
        this.ma50 = Optional.of(ma50);
    }

    public void setMa200(float ma200) {
        this.ma200 = Optional.of(ma200);
    }

    public void setDiffMa50Ma200(float diffMa50Ma200) {
        this.diffMa50Ma200 = Optional.of(diffMa50Ma200);
    }
}
