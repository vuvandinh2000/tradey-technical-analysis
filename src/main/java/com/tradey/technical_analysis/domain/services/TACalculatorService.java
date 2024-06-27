package com.tradey.technical_analysis.domain.services;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;

import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TACalculatorService {
    public Double calculateMovingAverage(List<OHLCVEntity> listOHLCV, int period) {
        if (listOHLCV.size() < period) {
            return null;
        }

        double ma = listOHLCV.stream()
                .map(OHLCVEntity::getClose)
                .toList()
                .subList(listOHLCV.size() - period, listOHLCV.size())
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);
        return Double.isNaN(ma) ? null : ma;
    }
}
