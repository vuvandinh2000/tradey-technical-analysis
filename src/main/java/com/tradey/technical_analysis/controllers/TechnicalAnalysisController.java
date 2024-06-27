package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.CalculateTAMetricService;
import com.tradey.technical_analysis.domain.services.StateMachineService;

public class TechnicalAnalysisController {
    private final StateMachineService stateMachineService;
    private final OHLCVService ohlcvService;
    private final CalculateTAMetricService technicalAnalysisService;

    public TechnicalAnalysisController(StateMachineService stateMachineService, OHLCVService ohlcvService, CalculateTAMetricService technicalAnalysisService) {
        this.stateMachineService = stateMachineService;
        this.ohlcvService = ohlcvService;
        this.technicalAnalysisService = technicalAnalysisService;
    }

    public void execute(String symbol) {
        TAStateMachineEntity taStateMachineEntity = stateMachineService.getTA(symbol);


    }
}
