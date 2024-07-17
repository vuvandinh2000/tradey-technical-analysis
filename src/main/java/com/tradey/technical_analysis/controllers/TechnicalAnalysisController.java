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

        // Calculate MA7
        Double ma7 = taCalculatorService.calculateMovingAverage(kLineEntityList, 7);
        if (ma7 == null) {
            String messageWarning = "KLine has less than 50 older KLines.";
            log.warn(messageWarning);
        }

        // Calculate MA25
        Double ma25 = taCalculatorService.calculateMovingAverage(kLineEntityList, 25);
        if (ma25 == null) {
            String messageWarning = "KLine has less than 50 older KLines.";
            log.warn(messageWarning);
        }

        // Calculate MA50
        Double ma50 = taCalculatorService.calculateMovingAverage(kLineEntityList, 50);
        if (ma50 == null) {
            String messageWarning = "KLine has less than 50 older KLines.";
            log.warn(messageWarning);
        }

        // Calculate MA99
        Double ma99 = taCalculatorService.calculateMovingAverage(kLineEntityList, 99);
        if (ma99 == null) {
            String messageWarning = "KLine has less than 99 older KLines.";
            log.warn(messageWarning);
        }

        // Calculate MA200
        Double ma200 = taCalculatorService.calculateMovingAverage(kLineEntityList, 200);
        if (ma200 == null) {
            String messageWarning = "KLine has less than 200 older KLines.";
            log.warn(messageWarning);
        }

        // Calculate diffMa25Ma99
        Double diffMa25Ma99 = null;
        if (ma25 != null && ma99 != null) {
            diffMa25Ma99 = ma25 - ma99;
        }

        // Calculate diffMa50Ma200
        Double diffMa50Ma200 = null;
        if (ma200 != null && ma50 != null) {
            diffMa50Ma200 = ma50 - ma200;
        }

        currentKLineEntity.setMa7(ma7);
        currentKLineEntity.setMa25(ma25);
        currentKLineEntity.setMa50(ma50);
        currentKLineEntity.setMa99(ma99);
        currentKLineEntity.setMa200(ma200);
        currentKLineEntity.setDiffMa25Ma99(diffMa25Ma99);
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
