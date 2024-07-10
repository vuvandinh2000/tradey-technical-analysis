package com.tradey.technical_analysis.config;

import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import com.tradey.technical_analysis.domain.services.TACalculatorService;
import dagger.Module;
import dagger.Provides;

import javax.inject.Inject;
import javax.inject.Singleton;

@Module
public class TechnicalAnalysisModule {
    @Singleton
    @Provides
    TACalculatorService taCalculatorService() {
        return new TACalculatorService();
    }

    @Singleton
    @Provides
    @Inject
    TechnicalAnalysisController technicalAnalysisController(TACalculatorService taCalculatorService) {
        return new TechnicalAnalysisController(taCalculatorService);
    }
}
