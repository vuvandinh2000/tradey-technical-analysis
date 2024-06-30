package com.tradey.technical_analysis.config;

import com.tradey.technical_analysis.TechnicalAnalysisLambdaHandler;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {TechnicalAnalysisModule.class})
public interface TechnicalAnalysisComponent {
    void inject(TechnicalAnalysisLambdaHandler requestHandler);
}
