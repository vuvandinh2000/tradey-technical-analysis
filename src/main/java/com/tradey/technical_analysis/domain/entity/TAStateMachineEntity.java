package com.tradey.technical_analysis.domain.entity;

import java.time.LocalDateTime;

public class TAStateMachineEntity {
    private String symbol;
    private String updatedAt;
    private String latestTimestampProcessed;

    public TAStateMachineEntity(String symbol, String updatedAt, String latestTimestampProcessed) {
        this.symbol = symbol;
        this.updatedAt = updatedAt;
        this.latestTimestampProcessed = latestTimestampProcessed;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getLatestTimestampProcessed() {
        return this.latestTimestampProcessed;
    }
}
