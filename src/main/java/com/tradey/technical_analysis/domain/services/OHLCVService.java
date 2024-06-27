package com.tradey.technical_analysis.domain.services;


import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.domain.repositories.OHLCVRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;

@RequiredArgsConstructor
public class OHLCVService {
    private final OHLCVRepository ohlcvRepository;

    public OHLCVEntity getBySymbolAndTimestamp(String symbol, String timestamp) {
        return ohlcvRepository.getBySymbolAndTimestamp(symbol, timestamp);
    }

    public List<OHLCVEntity> getAllBySymbolOlderThanTimestamp(String symbol, String timestamp, int limit){
        return ohlcvRepository.getAllBySymbolOlderThanTimestamp(symbol, timestamp, limit);
    }

    public OHLCVEntity update(OHLCVEntity ohlcv) {
        return ohlcvRepository.update(ohlcv);
    }
}
