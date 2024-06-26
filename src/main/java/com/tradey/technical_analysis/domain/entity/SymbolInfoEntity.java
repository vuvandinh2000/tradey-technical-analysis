package com.tradey.technical_analysis.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
public class SymbolInfoEntity {
    private final String exchangeType;
    private final String symbol;
    @Getter
    private final BigInteger onboardDate;
}
