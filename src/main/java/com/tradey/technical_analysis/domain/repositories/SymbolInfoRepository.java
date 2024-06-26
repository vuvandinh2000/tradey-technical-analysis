package com.tradey.technical_analysis.domain.repositories;

import com.tradey.technical_analysis.domain.entity.SymbolInfoEntity;

public interface SymbolInfoRepository {
    SymbolInfoEntity get(String exchangeType, String symbol);
}
