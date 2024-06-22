package com.tradey.technical_analysis.infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.tradey.technical_analysis.domain.entity.OHLCV;
import com.tradey.technical_analysis.domain.repositories.OHLCVRepository;

import java.util.*;
import java.util.logging.Logger;

public class DynamoOHLCVRepository implements OHLCVRepository {
    private final String table_name = "ohlcv_future";

    private final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    private final DynamoDB dynamodb = new DynamoDB(client);
    private final Table table = dynamodb.getTable(table_name);

    @Override
    public List<OHLCV> getAllFromTimestamp(String timestamp) {
        Map<String, Objects> valueMap = new HashMap<>();
        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("timestamp >= ")
                .withValueMap(valueMap);

        Iterable<Item> items = table.query(querySpec);
        List<OHLCV> ohlcvList = new ArrayList<>();
        for (Item item: items) {
            OHLCV ohlcv = new OHLCV();
            ohlcvList.add(ohlcv);
        }
        return ohlcvList;
    }

    @Override
    public List<OHLCV> updateBySymbolAndTimestamp() {
        return null;
    }
}
