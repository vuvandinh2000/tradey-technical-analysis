package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.tradey.technical_analysis.config.TechnicalAnalysisComponent;
import com.tradey.technical_analysis.config.DaggerTechnicalAnalysisComponent;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import com.tradey.technical_analysis.infrastructure.dto.HTTPResponse;
import com.tradey.technical_analysis.infrastructure.dto.TechnicalAnalysisEventDTO;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TechnicalAnalysisLambdaHandler implements RequestHandler<Map<String, Object>, HTTPResponse> {
    @Inject
    TechnicalAnalysisController technicalAnalysisController;

    public TechnicalAnalysisLambdaHandler() {
        TechnicalAnalysisComponent technicalAnalysisComponent = DaggerTechnicalAnalysisComponent.builder().build();
        technicalAnalysisComponent.inject(this);
    }


    @Override
    public HTTPResponse handleRequest(Map<String, Object> event, Context context) {
        TechnicalAnalysisEventDTO technicalAnalysisEventDTO = TechnicalAnalysisEventDTO.fromEventMap(event);

        return technicalAnalysisController.execute(technicalAnalysisEventDTO);
    }

    public static void main(String[] args) {
        TechnicalAnalysisLambdaHandler technicalAnalysisLambdaHandler = new TechnicalAnalysisLambdaHandler();
        Map<String, Object> event = new HashMap<>();
        event.put("exchange_type", "FUTURES-U_MARGINED");
        event.put("symbol", "BTCUSDT");
        event.put("timestamp", "2021-12-20T13:59:59.999000+00:00");
        event.put("force", true);

        HTTPResponse response = technicalAnalysisLambdaHandler.handleRequest(event, null);
        System.out.println(response.getMessage());
    }
}
