package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.tradey.technical_analysis.config.TechnicalAnalysisComponent;
import com.tradey.technical_analysis.config.DaggerTechnicalAnalysisComponent;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import com.tradey.technical_analysis.infrastructure.dto.HTTPResponse;
import com.tradey.technical_analysis.infrastructure.dto.TechnicalAnalysisEventDTO;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        List<Map<String, Object>> latestKLines = new ArrayList<>();

        Map<String, Object> kline1 = new HashMap<>();
        kline1.put("open", "62670.01000000");
        kline1.put("high", "62801.05000000");
        kline1.put("low", "62485.63000000");
        kline1.put("close", "62715.98000000");
        kline1.put("volume", "787.71878000");
        kline1.put("openTime", 1719914400000L);
        kline1.put("closeTime", 1719917999999L);
        kline1.put("quoteAssetVolume", "49341118.41858720");
        kline1.put("numberOfTrades", 44740);
        kline1.put("takerBuyBaseAssetVolume", "414.63212000");
        kline1.put("takerBuyQuoteAssetVolume", "25969685.37737870");

        latestKLines.add(kline1);

        event.put("latestKLines", latestKLines);

        HTTPResponse response = technicalAnalysisLambdaHandler.handleRequest(event, null);
        System.out.println(response.getData());
    }
}
