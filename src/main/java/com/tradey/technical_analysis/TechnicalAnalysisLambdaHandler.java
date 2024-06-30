package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.tradey.technical_analysis.config.TechnicalAnalysisComponent;
import com.tradey.technical_analysis.config.DaggerTechnicalAnalysisComponent;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import lombok.extern.slf4j.Slf4j;
import javax.inject.Inject;


@Slf4j
public class TechnicalAnalysisLambdaHandler implements RequestHandler<Object, Void> {
    @Inject
    TechnicalAnalysisController technicalAnalysisController;

    public TechnicalAnalysisLambdaHandler() {
        TechnicalAnalysisComponent technicalAnalysisComponent = DaggerTechnicalAnalysisComponent.builder().build();
        technicalAnalysisComponent.inject(this);
    }


    @Override
    public Void handleRequest(Object event, Context context) {
        log.info("Start execute Technical Analysis...");
        technicalAnalysisController.execute("FUTURES-U_MARGINED", "BTCUSDT");
        return null;
    }
}
