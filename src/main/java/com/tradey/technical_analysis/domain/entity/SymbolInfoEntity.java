package com.tradey.technical_analysis.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SymbolInfoEntity {
    private final String exchangeType;
    private final String symbol;
    @Getter
    private final long onboardDate;
}
