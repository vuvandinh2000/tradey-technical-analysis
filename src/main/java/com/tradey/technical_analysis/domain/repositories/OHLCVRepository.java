package com.tradey.technical_analysis.domain.repositories;

import java.util.List;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;

public interface OHLCVRepository {
    OHLCVEntity getBySymbolAndTimestamp(String symbol, String timestamp);
    List<OHLCVEntity> getAllBySymbolAndLETimestamp(String symbol, String timestamp, int limit);
    OHLCVEntity updateTAMetrics(String symbol, String timestamp, Double ma50, Double ma200, Double diffMa50Ma200);
}
