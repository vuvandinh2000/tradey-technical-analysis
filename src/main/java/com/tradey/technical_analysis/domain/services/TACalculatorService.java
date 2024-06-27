package com.tradey.technical_analysis.domain.services;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;

import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TACalculatorService {
    public Double calculateMovingAverage(List<OHLCVEntity> ohlcvEntityList, int period) {
        if (ohlcvEntityList.size() < period) {
            return null;
        }

        double ma = ohlcvEntityList.stream()
                .map(OHLCVEntity::getClose)
                .toList()
                .subList(ohlcvEntityList.size() - period, ohlcvEntityList.size())
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);
        return Double.isNaN(ma) ? null : ma;
    }
}
