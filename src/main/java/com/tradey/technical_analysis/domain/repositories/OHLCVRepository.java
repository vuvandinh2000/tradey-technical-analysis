package com.tradey.technical_analysis.domain.repositories;

import java.util.List;

import com.tradey.technical_analysis.domain.entity.OHLCV;

public interface OHLCVRepository {
    OHLCV getBySymbolAndTimestamp(String symbol, String timestamp);
    List<OHLCV> getAllBySymbolOlderThanTimestamp(String symbol, String timestamp, int limit);
    OHLCV update(OHLCV ohlcv);
}
