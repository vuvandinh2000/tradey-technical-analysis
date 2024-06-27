package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.SymbolInfoService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;
import com.tradey.technical_analysis.domain.services.StateMachineService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
public class TechnicalAnalysisController {
    private final StateMachineService stateMachineService;
    private final SymbolInfoService symbolInfoService;
    private final OHLCVService ohlcvService;
    private final TACalculatorService technicalAnalysisService;

    public void execute(String exchangeType, String symbol) {
        TAStateMachineEntity taStateMachineEntity = stateMachineService.getTA(symbol);

        // Find the oldest timestamp of OHLCV that has not calculated TA Metrics yet.
        String currentTimestamp;
        if (taStateMachineEntity != null) {
            long latestTimestampProcessed = Long.parseLong(taStateMachineEntity.getLatestTimestampProcessed());
            currentTimestamp = Long.toString(latestTimestampProcessed + 3600);
        }
        else {
            Long onboardDate = symbolInfoService.getOnboardDate(exchangeType, symbol);
            if (onboardDate == null) {
                String messageError = String.format("Can not find onboard date of symbol='%s' in exchange='%s'", exchangeType, symbol);
                log.error(messageError);
                throw new NoSuchElementException(messageError);
            }
            currentTimestamp = Long.toString(onboardDate);
        }

        OHLCVEntity ohlcvEntity = ohlcvService.getBySymbolAndTimestamp(symbol, currentTimestamp);

        if (ohlcvEntity != null) {
            if (ohlcvEntity.hasAllTAMetricsAreNull()) {
                // All TAMetrics are null: get all OHLCV are older than currentTimestamp
                List<OHLCVEntity> ohlcvEntityList = ohlcvService.getAllBySymbolOlderThanTimestamp(symbol, currentTimestamp, 200);

                // Calculate MA50, MA200, diffMa50Ma200
                Double diffMa50Ma200 = null;

                Double ma50 = technicalAnalysisService.calculateMovingAverage(ohlcvEntityList, 50);
                if (ma50 == null) {
                    String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has less than 50 older OHLCV.", symbol, currentTimestamp);
                    log.warn(messageWarning);
                }
                Double ma200 = technicalAnalysisService.calculateMovingAverage(ohlcvEntityList, 200);
                if (ma200 == null) {
                    String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has less than 200 older OHLCV.", symbol, currentTimestamp);
                    log.warn(messageWarning);
                }
                if (ma200 != null && ma50 != null) {
                    diffMa50Ma200 = ma50 - ma200;
                }

                OHLCVEntity updatedOHLCVEntity = ohlcvService.updateTAMetricsBySymbolAndTimestamp(symbol, currentTimestamp, ma50, ma200, diffMa50Ma200);

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
            else {
                String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has unprocessed state but exists TAMetrics.", symbol, currentTimestamp);
                log.warn(messageWarning);
            }
        }
        else {
            String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has not crawled from '%s' yet.", symbol, currentTimestamp, exchangeType);
            log.warn(messageWarning);
            return;
        }

        // Update state: latestTimestampProcessed = currentTimestamp
        if (taStateMachineEntity == null) {
            taStateMachineEntity = new TAStateMachineEntity(symbol, Long.toString(System.currentTimeMillis()));
        }
        taStateMachineEntity.setLatestTimestampProcessed(currentTimestamp);
        stateMachineService.upsertTA(symbol, taStateMachineEntity);
    }
}
