package com.tradey.technical_analysis.domain.repositories;

import java.util.List;

import com.tradey.technical_analysis.domain.entity.OHLCV;

public interface OHLCVRepository {
    List<OHLCV> getAllFromTimestamp(String timestamp);
    List<OHLCV> updateBySymbolAndTimestamp();
}
