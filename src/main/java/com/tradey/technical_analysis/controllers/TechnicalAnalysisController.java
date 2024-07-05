package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;

import com.tradey.technical_analysis.infrastructure.dto.HTTPResponse;
import com.tradey.technical_analysis.infrastructure.dto.TechnicalAnalysisEventDTO;
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

    public HTTPResponse execute(TechnicalAnalysisEventDTO eventDTO) {
        if (eventDTO.getSymbol() == null) {
            return new HTTPResponse(
                    HttpStatus.SC_BAD_REQUEST,
                    "Invalid params: 'symbol' is required.",
                    new HashMap<>(),
                    Time.getCurrentTimestamp().format(Time.formatter)
            );
        }
        if (eventDTO.getTimestamp() == null) {
            return new HTTPResponse(
                    HttpStatus.SC_BAD_REQUEST,
                    "Invalid params: 'timestamp' is required.",
                    new HashMap<>(),
                    Time.getCurrentTimestamp().format(Time.formatter)
            );
        }

        log.info(String.format("Handling for symbol='%s', timestamp='%s'...", eventDTO.getSymbol(), eventDTO.getTimestamp()));
        OHLCVEntity ohlcvEntity = ohlcvService.getBySymbolAndTimestamp(eventDTO.getSymbol(), eventDTO.getTimestamp());

        HashMap<String, Object> returned_data = new HashMap<>();
        returned_data.put("exchange_type", eventDTO.getExchangeType());
        returned_data.put("symbol", eventDTO.getSymbol());
        returned_data.put("timestamp", eventDTO.getTimestamp());

        if (ohlcvEntity != null) {
            if (ohlcvEntity.hasAllTAMetricsAreNull() || eventDTO.isForce()) {
                // All TAMetrics are null: get all OHLCV are older than currentTimestamp
                List<OHLCVEntity> ohlcvEntityList = ohlcvService.getAllBySymbolAndLETimestamp(eventDTO.getSymbol(), eventDTO.getTimestamp(), 200);

                // Calculate MA50, MA200, diffMa50Ma200
                Double diffMa50Ma200 = null;

                Double ma50 = technicalAnalysisService.calculateMovingAverage(ohlcvEntityList, 50);
                if (ma50 == null) {
                    String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has less than 50 older OHLCV.", eventDTO.getSymbol(), eventDTO.getTimestamp());
                    log.warn(messageWarning);
                }
                Double ma200 = technicalAnalysisService.calculateMovingAverage(ohlcvEntityList, 200);
                if (ma200 == null) {
                    String messageWarning = String.format("OHLCV of symbol='%s', timestamp='%s' has less than 200 older OHLCV.", eventDTO.getSymbol(), eventDTO.getTimestamp());
                    log.warn(messageWarning);
                }
                if (ma200 != null && ma50 != null) {
                    diffMa50Ma200 = ma50 - ma200;
                }

                OHLCVEntity updatedOHLCVEntity = ohlcvService.updateTAMetricsBySymbolAndTimestamp(eventDTO.getSymbol(), eventDTO.getTimestamp(), ma50, ma200, diffMa50Ma200);

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
