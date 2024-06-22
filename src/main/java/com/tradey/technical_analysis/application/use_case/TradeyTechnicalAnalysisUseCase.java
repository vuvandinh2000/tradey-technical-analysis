package com.tradey.technical_analysis.application.use_case;

import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.CalculateTAMetricService;

public class TradeyTechnicalAnalysisUseCase {
    private final OHLCVService ohlcvService;
    private final CalculateTAMetricService technicalAnalysisService;

    public TradeyTechnicalAnalysisUseCase(OHLCVService ohlcvService, CalculateTAMetricService technicalAnalysisService) {
        this.ohlcvService = ohlcvService;
        this.technicalAnalysisService = technicalAnalysisService;
    }

    public void execute() {
    }
}
