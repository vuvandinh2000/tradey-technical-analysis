package com.tradey.technical_analysis.domain.services;

import com.tradey.technical_analysis.domain.entity.SymbolInfoEntity;
import com.tradey.technical_analysis.domain.repositories.SymbolInfoRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SymbolInfoService {
    private final SymbolInfoRepository symbolInfoRepository;

    public Long getOnboardDate(String exchangeType, String symbol) {
        SymbolInfoEntity entity = symbolInfoRepository.get(exchangeType, symbol);
        return entity == null ? null : entity.getOnboardDate();
    }
}
