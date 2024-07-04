package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;

import com.tradey.technical_analysis.infrastructure.dto.HTTPResponse;
import com.tradey.technical_analysis.pkgs.Time;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TechnicalAnalysisController {
    private final OHLCVService ohlcvService;
    private final TACalculatorService technicalAnalysisService;

    public HTTPResponse execute(String exchangeType, String symbol, String timestamp, boolean force) {
        log.info(String.format("Handling for symbol='%s', timestamp='%s'...", symbol, timestamp));
        OHLCVEntity ohlcvEntity = ohlcvService.getBySymbolAndTimestamp(symbol, timestamp);

        HashMap<String, Object> returned_data = new HashMap<>();
        returned_data.put("exchange_type", exchangeType);
        returned_data.put("symbol", symbol);
        returned_data.put("timestamp", timestamp);

        if (ohlcvEntity != null) {
            if (ohlcvEntity.hasAllTAMetricsAreNull() || force) {
                // All TAMetrics are null: get all OHLCV are older than currentTimestamp
                List<OHLCVEntity> ohlcvEntityList = ohlcvService.getAllBySymbolAndLETimestamp(symbol, timestamp, 200);

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
                    returned_data.put("ma50", updatedOHLCVEntity.getMa50());
                    returned_data.put("ma200", updatedOHLCVEntity.getMa200());
                    returned_data.put("diff_ma50_ma200", updatedOHLCVEntity.getDiffMa50Ma200());
                    return new HTTPResponse(
                            HttpStatus.SC_OK,
                            "Successfully calculate Technical Analysis Metrics.",
                            returned_data,
                            Time.getCurrentTimestamp().format(Time.formatter)
                        );
                }
            }
            else {
                return new HTTPResponse(
                        HttpStatus.SC_CONFLICT,
                        "OHLCV has already been calculated Technical Analysis Metrics.",
                        returned_data,
                        Time.getCurrentTimestamp().format(Time.formatter)
                );
            }
        }
        else {
            return new HTTPResponse(
                    HttpStatus.SC_NOT_FOUND,
                    "OHLCV has not been crawled yet.",
                    returned_data,
                    Time.getCurrentTimestamp().format(Time.formatter)
            );
        }
        return null;
    }
}
