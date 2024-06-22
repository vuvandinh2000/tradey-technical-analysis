package com.tradey.technical_analysis;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;


public class TradeyTechnicalAnalysisApplication implements RequestHandler {
	
	@Override

	public void handleRequest(event, Context context) {
		LambdaLogger logger = context.getLogger();
		SpringApplication.run(TradeyTechnicalAnalysisApplication.class, args);
	}
}
