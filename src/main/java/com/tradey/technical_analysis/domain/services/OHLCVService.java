package com.tradey.technical_analysis.domain.services;


import com.tradey.technical_analysis.domain.entity.OHLCV;
import com.tradey.technical_analysis.domain.repositories.OHLCVRepository;

import java.util.List;

public class OHLCVService {
    private final OHLCVRepository ohlcvRepository;

    public OHLCVService(OHLCVRepository ohlcvRepository) {
        this.ohlcvRepository = ohlcvRepository;
    }

    public List<OHLCV> getOHLCVBatch(String timestamp) {
        return ohlcvRepository.getAllFromTimestamp(timestamp);
    }
}
