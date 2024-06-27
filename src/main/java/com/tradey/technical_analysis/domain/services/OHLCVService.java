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
        return ohlcvRepository.getAllBySymbolOlderEqualThanTimestamp(symbol, timestamp, limit);
    }

    public OHLCVEntity updateTAMetricsBySymbolAndTimestamp(String symbol, String timestamp, Double ma50, Double ma200, Double diffMa50Ma200) {
        return ohlcvRepository.updateTAMetricsBySymbolAndTimestamp(symbol, timestamp, ma50, ma200, diffMa50Ma200);
    }
}
