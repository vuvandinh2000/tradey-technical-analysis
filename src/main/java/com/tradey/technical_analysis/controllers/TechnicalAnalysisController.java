package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TechnicalAnalysisController {
    private final OHLCVService ohlcvService;
    private final TACalculatorService technicalAnalysisService;

    public void execute(String exchangeType, String symbol, String timestamp) {
        log.info(String.format("Handling for symbol='%s', timestamp='%s'...", symbol, timestamp));
        OHLCVEntity ohlcvEntity = ohlcvService.getBySymbolAndTimestamp(symbol, timestamp);

        if (ohlcvEntity != null) {
            if (ohlcvEntity.hasAllTAMetricsAreNull()) {
                // All TAMetrics are null: get all OHLCV are older than currentTimestamp
                List<OHLCVEntity> ohlcvEntityList = ohlcvService.getAllBySymbolOlderThanTimestamp(symbol, timestamp, 200);

                // Calculate MA50, MA200, diffMa50Ma200
                Double diffMa50Ma200 = null;

                Double ma50 = technicalAnalysisService.calculateMovingAverage(ohlcvEntityList, 50);
                if (ma50 == null) {
                    String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has less than 50 older OHLCV.", symbol, timestamp);
                    log.warn(messageWarning);
                }
                Double ma200 = technicalAnalysisService.calculateMovingAverage(ohlcvEntityList, 200);
                if (ma200 == null) {
                    String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has less than 200 older OHLCV.", symbol, timestamp);
                    log.warn(messageWarning);
                }
                if (ma200 != null && ma50 != null) {
                    diffMa50Ma200 = ma50 - ma200;
                }

                OHLCVEntity updatedOHLCVEntity = ohlcvService.updateTAMetricsBySymbolAndTimestamp(symbol, timestamp, ma50, ma200, diffMa50Ma200);

                if (updatedOHLCVEntity != null) {
                    String messageInfo = String.format(
                            "Successfully updated for OHLCV of symbol='%s', timestamp='%s', MA50='%f', MA200='%f', MA50-MA200='%f'.",
                            updatedOHLCVEntity.getSymbol(),
                            updatedOHLCVEntity.getTimestamp(),
                            updatedOHLCVEntity.getMa50(),
                            updatedOHLCVEntity.getMa200(),
                            updatedOHLCVEntity.getDiffMa50Ma200()
                    );
                    log.info(messageInfo);
                }
            }
            else {
                String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has unprocessed state but exists TAMetrics.", symbol, timestamp);
                log.warn(messageWarning);
            }
        }
        else {
            String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has not crawled from '%s' yet.", symbol, timestamp, exchangeType);
            log.warn(messageWarning);
        }
    }
}
