package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.KLineEntity;
import com.tradey.technical_analysis.domain.services.TACalculatorService;

import com.tradey.technical_analysis.infrastructure.dto.HTTPResponse;
import com.tradey.technical_analysis.infrastructure.dto.TechnicalAnalysisEventDTO;
import com.tradey.technical_analysis.pkgs.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TechnicalAnalysisController {
    private final TACalculatorService taCalculatorService;

    public HTTPResponse execute(TechnicalAnalysisEventDTO eventDTO) {
        List<KLineEntity> kLineEntityList = eventDTO.getLatestKLines();
        KLineEntity currentKLineEntity = kLineEntityList.getLast();

        // Calculate MA50, MA200, diffMa50Ma200
        Double diffMa50Ma200 = null;

        Double ma50 = taCalculatorService.calculateMovingAverage(kLineEntityList, 50);
        if (ma50 == null) {
            String messageWarning = "KLine has less than 50 older KLines.";
            log.warn(messageWarning);
        }
        Double ma200 = taCalculatorService.calculateMovingAverage(kLineEntityList, 200);
        if (ma200 == null) {
            String messageWarning = "OHLCV of symbol='%s', timestamp='%s' has less than 200 older OHLCV.";
            log.warn(messageWarning);
        }
        if (ma200 != null && ma50 != null) {
            diffMa50Ma200 = ma50 - ma200;
        }

        currentKLineEntity.setMa50(ma50);
        currentKLineEntity.setMa200(ma200);
        currentKLineEntity.setDiffMa50Ma200(diffMa50Ma200);

        return new HTTPResponse(
                200,
                "Successfully calculate Technical Analysis Metrics.",
                eventDTO.getProperties(),
                currentKLineEntity.toMap(),
                Time.getCurrentTimestamp().format(Time.formatter)
        );
    }
}
