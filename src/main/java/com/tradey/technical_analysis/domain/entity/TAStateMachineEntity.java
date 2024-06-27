package com.tradey.technical_analysis.domain.entity;

import lombok.Setter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TAStateMachineEntity {
    private final String symbol;
    private final String updatedAt;
    @Setter
    private String latestTimestampProcessed;
}
