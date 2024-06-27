package com.tradey.technical_analysis.controllers;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;
import com.tradey.technical_analysis.domain.services.StateMachineService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TechnicalAnalysisController {
    private final StateMachineService stateMachineService;
    private final OHLCVService ohlcvService;
    private final TACalculatorService technicalAnalysisService;

    public void execute(String symbol) {
        TAStateMachineEntity taStateMachineEntity = stateMachineService.getTA(symbol);


    }
}
