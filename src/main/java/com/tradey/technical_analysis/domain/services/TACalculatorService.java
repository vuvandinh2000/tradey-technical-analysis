package com.tradey.technical_analysis.domain.services;

import com.tradey.technical_analysis.domain.entity.KLineEntity;

import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TACalculatorService {
    public Double calculateMovingAverage(List<KLineEntity> KLineEntityList, int period) {
        if (KLineEntityList.size() < period) {
            return null;
        }

        double ma = KLineEntityList.stream()
                .map(KLineEntity::getClose)
                .toList()
                .subList(KLineEntityList.size() - period, KLineEntityList.size())
                .stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.NaN);
        return Double.isNaN(ma) ? null : ma;
    }
}
