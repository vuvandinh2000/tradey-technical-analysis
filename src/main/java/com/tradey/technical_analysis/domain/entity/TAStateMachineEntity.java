package com.tradey.technical_analysis.domain.entity;

public class TAStateMachineEntity {
    private final String symbol;
    private String updatedAt;
    private String latestTimestampProcessed;

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setLatestTimestampProcessed(String latestTimestampProcessed) {
        this.latestTimestampProcessed = latestTimestampProcessed;
    }

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
