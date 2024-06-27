package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.amazonaws.services.lambda.runtime.logging.LogLevel;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;


public class TechnicalAnalysisLambdaHandler implements RequestHandler<Object, Void> {
    TechnicalAnalysisController technicalAnalysisUseCase;


    @Override

    public Void handleRequest(Object event, Context context) {
        LambdaLogger logger = context.getLogger();
        logger.log("Start execute Technical Analysis...", LogLevel.INFO);
        technicalAnalysisUseCase.execute("BTCUSDT");
        return null;
    }
}
