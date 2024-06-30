package com.tradey.technical_analysis.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.tradey.technical_analysis.controllers.TechnicalAnalysisController;
import com.tradey.technical_analysis.domain.services.OHLCVService;
import com.tradey.technical_analysis.domain.services.StateMachineService;
import com.tradey.technical_analysis.domain.services.SymbolInfoService;
import com.tradey.technical_analysis.domain.services.TACalculatorService;
import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoOHLCVRepository;
import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoStateMachineRepository;
import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoSymbolInfoRepository;
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
    DynamoSymbolInfoRepository dynamoSymbolInfoRepository(DynamoDB dynamoDB) {
        return new DynamoSymbolInfoRepository(dynamoDB);
    }

    @Singleton
    @Provides
    DynamoStateMachineRepository dynamoStateMachineRepository(DynamoDB dynamoDB) {
        return new DynamoStateMachineRepository(dynamoDB);
    }

    @Singleton
    @Provides
    DynamoOHLCVRepository dynamoOHLCVRepository(DynamoDB dynamoDB) {
        return new DynamoOHLCVRepository(dynamoDB);
    }

    @Singleton
    @Provides
    SymbolInfoService symbolInfoService(DynamoSymbolInfoRepository dynamoSymbolInfoRepository) {
        return new SymbolInfoService(dynamoSymbolInfoRepository);
    }

    @Singleton
    @Provides
    StateMachineService stateMachineService(DynamoStateMachineRepository dynamoStateMachineRepository) {
        return new StateMachineService(dynamoStateMachineRepository);
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
            StateMachineService stateMachineService,
            SymbolInfoService symbolInfoService,
            OHLCVService ohlcvService,
            TACalculatorService taCalculatorService
            ) {
        return new TechnicalAnalysisController(stateMachineService, symbolInfoService, ohlcvService, taCalculatorService);
    }
}
