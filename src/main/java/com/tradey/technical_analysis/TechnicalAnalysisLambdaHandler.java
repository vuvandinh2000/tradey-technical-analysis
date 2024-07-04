package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.tradey.technical_analysis.config.TechnicalAnalysisComponent;
import com.tradey.technical_analysis.config.DaggerTechnicalAnalysisComponent;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import com.tradey.technical_analysis.infrastructure.dto.HTTPResponse;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TechnicalAnalysisLambdaHandler implements RequestHandler<Map<String, String>, HTTPResponse> {
    @Inject
    TechnicalAnalysisController technicalAnalysisController;

    public TechnicalAnalysisLambdaHandler() {
        TechnicalAnalysisComponent technicalAnalysisComponent = DaggerTechnicalAnalysisComponent.builder().build();
        technicalAnalysisComponent.inject(this);
    }


    @Override
    public HTTPResponse handleRequest(Map<String, String> event, Context context) {
        String exchangeType = event.get("exchange_type");
        String symbol = event.get("symbol");
        String timestamp = event.get("timestamp");
        boolean force = Boolean.parseBoolean(event.get("force"));
        if (exchangeType == null) {
            exchangeType = "FUTURES-U_MARGINED";
            log.warn(String.format("Event has 'exchange_type' is null, use default='%s'", exchangeType));
        }
        if (symbol == null) {
            log.error("'symbol' is required in input event.");
        }
        if (timestamp == null) {
            log.error("'timestamp' is required in input event.");
        }
        log.info("Start execute Technical Analysis...");
        return technicalAnalysisController.execute(exchangeType, symbol, timestamp, force);
    }

    public static void main(String[] args) {
        TechnicalAnalysisLambdaHandler technicalAnalysisLambdaHandler = new TechnicalAnalysisLambdaHandler();
        Map<String, String> event = new HashMap<>();
        event.put("exchange_type", "FUTURES-U_MARGINED");
        event.put("symbol", "BTCUSDT");
        event.put("timestamp", "2021-12-20T13:59:59.999000+00:00");
        event.put("force", "true");

        technicalAnalysisLambdaHandler.handleRequest(event, null);
    }
}
