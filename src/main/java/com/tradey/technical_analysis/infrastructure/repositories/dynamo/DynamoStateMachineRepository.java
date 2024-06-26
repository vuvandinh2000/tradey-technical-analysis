package com.tradey.technical_analysis.infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.domain.repositories.StateMachineRepository;
import com.tradey.technical_analysis.infrastructure.dto.StateMachineDTO;

import java.util.*;

import static com.tradey.technical_analysis.pkgs.Constants.serviceName;
import static com.tradey.technical_analysis.pkgs.Constants.stateMachineTableName;

public class DynamoStateMachineRepository implements StateMachineRepository {

    private final Table table;

    public DynamoStateMachineRepository(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable(stateMachineTableName);
    }

    @Override
    public TAStateMachineEntity getTA(String symbol) {
        Item item = table.getItem(new PrimaryKey("service", serviceName, "symbol", symbol));

        if (item == null) {
            return null;
        }

        String service = item.getString("service");
        Map<String, Object> state = item.getMap("state");
        String updatedAt = item.getString("updated_at");

        return new StateMachineDTO(service, symbol, state, updatedAt).toEntity();
    }

    @Override
    public void upsertTA(String symbol, TAStateMachineEntity entity) {
        StateMachineDTO dto = StateMachineDTO.fromEntity(entity);
        Item item = new Item()
                .withPrimaryKey("service", dto.getService(), "symbol", dto.getSymbol())
                .withMap("state", dto.getState())
                .withString("updated_at", dto.getUpdated_at());
        table.putItem(item);
    }
}
