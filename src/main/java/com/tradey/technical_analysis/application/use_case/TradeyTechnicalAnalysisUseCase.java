package com.tradey.technical_analysis.application.use_case;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.CalculateTAMetricService;
import com.tradey.technical_analysis.domain.services.StateMachineService;

public class TradeyTechnicalAnalysisUseCase {
    private final StateMachineService stateMachineService;
    private final OHLCVService ohlcvService;
    private final CalculateTAMetricService technicalAnalysisService;

    public TradeyTechnicalAnalysisUseCase(StateMachineService stateMachineService, OHLCVService ohlcvService, CalculateTAMetricService technicalAnalysisService) {
        this.stateMachineService = stateMachineService;
        this.ohlcvService = ohlcvService;
        this.technicalAnalysisService = technicalAnalysisService;
    }

    public void execute(String symbol) {
        TAStateMachineEntity taStateMachineEntity = stateMachineService.getTA(symbol);


    }
}
