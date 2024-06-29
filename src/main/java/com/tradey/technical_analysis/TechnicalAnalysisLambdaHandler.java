package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TechnicalAnalysisLambdaHandler implements RequestHandler<Object, Void> {
    TechnicalAnalysisController technicalAnalysisController;


    @Override

    public Void handleRequest(Object event, Context context) {
        log.info("Start execute Technical Analysis...");
        technicalAnalysisController.execute("BINANCE", "BTCUSDT");
        return null;
    }
}
