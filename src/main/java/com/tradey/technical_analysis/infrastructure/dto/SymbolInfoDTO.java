package com.tradey.technical_analysis.infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.SymbolInfoEntity;

import lombok.RequiredArgsConstructor;

import java.math.BigInteger;

@RequiredArgsConstructor
public class SymbolInfoDTO {
    private final String exchange_type;
    private final String symbol;
    private final BigInteger onboard_date;

    public SymbolInfoEntity toEntity() {
        return new SymbolInfoEntity(exchange_type, symbol, onboard_date);
    }
}
