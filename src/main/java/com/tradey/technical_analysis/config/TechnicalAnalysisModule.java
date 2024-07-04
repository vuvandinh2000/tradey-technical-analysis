package com.tradey.technical_analysis.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;
import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoOHLCVRepository;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class TechnicalAnalysisModule {
    @Singleton
    @Provides
    DynamoDB dynamoDB() {
        return new DynamoDB(Regions.AP_SOUTHEAST_1);
    }

    @Singleton
    @Provides
    DynamoOHLCVRepository dynamoOHLCVRepository(DynamoDB dynamoDB) {
        return new DynamoOHLCVRepository(dynamoDB);
    }

    @Singleton
    @Provides
    OHLCVService ohlcvService(DynamoOHLCVRepository dynamoOHLCVRepository) {
        return new OHLCVService(dynamoOHLCVRepository);
    }

    @Singleton
    @Provides
    TACalculatorService taCalculatorService() {
        return new TACalculatorService();
    }

    @Singleton
    @Provides
    TechnicalAnalysisController technicalAnalysisController(
            OHLCVService ohlcvService,
            TACalculatorService taCalculatorService
            ) {
        return new TechnicalAnalysisController(ohlcvService, taCalculatorService);
    }
}
