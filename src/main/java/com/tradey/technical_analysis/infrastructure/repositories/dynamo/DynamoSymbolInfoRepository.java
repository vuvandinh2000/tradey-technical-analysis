package com.tradey.technical_analysis.infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.tradey.technical_analysis.domain.entity.SymbolInfoEntity;
import com.tradey.technical_analysis.domain.repositories.SymbolInfoRepository;
import com.tradey.technical_analysis.infrastructure.dto.SymbolInfoDTO;

import static com.tradey.technical_analysis.pkgs.Constants.*;

public class DynamoSymbolInfoRepository implements SymbolInfoRepository {
    private final Table table;

    public DynamoSymbolInfoRepository(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable(symbolInfoTableName);
    }

    @Override
    public SymbolInfoEntity get(String exchangeType, String symbol) {
        Item item = table.getItem(new PrimaryKey("exchange_type", exchangeType, "symbol", symbol));

        if (item == null) {
            return null;
        }

        long onboardDate = item.getLong("onboard_date");

        return new SymbolInfoDTO(exchangeType, symbol, onboardDate).toEntity();
    }
}
