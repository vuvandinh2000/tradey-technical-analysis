package com.tradey.technical_analysis.domain.repositories;

import java.util.List;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;

public interface OHLCVRepository {
    OHLCVEntity getBySymbolAndTimestamp(String symbol, String timestamp);
    List<OHLCVEntity> getAllBySymbolOlderThanTimestamp(String symbol, String timestamp, int limit);
    OHLCVEntity update(OHLCVEntity ohlcv);
}
