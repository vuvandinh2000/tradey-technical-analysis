package com.tradey.technical_analysis.infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.domain.repositories.OHLCVRepository;
import com.tradey.technical_analysis.infrastructure.dto.OHLCVDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.tradey.technical_analysis.pkgs.Constants.ohlcvTableName;

@Slf4j
public class DynamoOHLCVRepository implements OHLCVRepository {

    private final Table table;

    public DynamoOHLCVRepository(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable(ohlcvTableName);
    }

    @Override
    public OHLCVEntity getBySymbolAndTimestamp(String symbol, String timestamp) {
        Item item = table.getItem(new PrimaryKey("symbol", symbol, "timestamp", timestamp));
        if (item == null) {
            String messageWarning = String.format("Not existed OHLCV with symbol='%s', timestamp='%s'", symbol, timestamp);
            log.warn(messageWarning);
        }
        return OHLCVItemToEntity(item);
    }

    @Override
    public List<OHLCVEntity> getAllBySymbolOlderEqualThanTimestamp(String symbol, String timestamp, int limit) {
        RangeKeyCondition rangeKeyCondition = new RangeKeyCondition("timestamp").le(timestamp);
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("symbol", symbol)
                .withRangeKeyCondition(rangeKeyCondition)
                .withMaxResultSize(limit);
        Iterable<Item> items = this.table.query(querySpec);

        List<OHLCVEntity> result = new ArrayList<>();
        for (Item item: items) {
            result.add(OHLCVItemToEntity(item));
        }

        return result;
    }

    @Override
    public OHLCVEntity updateTAMetricsBySymbolAndTimestamp(String symbol, String timestamp, Double ma50, Double ma200, Double diffMa50Ma200) {
        if (ma50 == null && ma200 == null && diffMa50Ma200 == null) {
            String messageWarning = String.format("Skipped update OHLCV due to null of MA50, MA200 and Cross(MA50, MA200) for symbol='%s', timestamp='%s", symbol, timestamp);
            log.warn(messageWarning);
            return null;
        }

        UpdateItemSpec updateItemSpec = new UpdateItemSpec()
                .withPrimaryKey("symbol", symbol, "timestamp", timestamp);

        if (ma50 != null) {
            updateItemSpec.withAttributeUpdate(new AttributeUpdate("ma50").put(ma50));
        }
        else {
            String messageWarning = String.format("Not existed MA50 of symbol='%s', timestamp='%s", symbol, timestamp);
            log.warn(messageWarning);
        }
        if (ma200 != null) {
            updateItemSpec.withAttributeUpdate(new AttributeUpdate("ma200").put(ma200));
        }
        else {
            String messageWarning = String.format("Not existed MA200 of symbol='%s', timestamp='%s", symbol, timestamp);
            log.warn(messageWarning);
        }
        if (diffMa50Ma200 != null) {
            updateItemSpec.withAttributeUpdate(new AttributeUpdate("diff_ma50_ma200").put(diffMa50Ma200));
        }
        else {
            String messageWarning = String.format("Not existed Cross(MA50, MA200) of symbol='%s', timestamp='%s", symbol, timestamp);
            log.warn(messageWarning);
        }

        updateItemSpec.withReturnValues(ReturnValue.ALL_NEW);
        UpdateItemOutcome outcome = this.table.updateItem(updateItemSpec);
        return OHLCVItemToEntity(outcome.getItem());
    }

    private OHLCVEntity OHLCVItemToEntity(Item item) {
        if (item == null) {
            return null;
        }

        OHLCVDTO ohlcvDTO = OHLCVDTO.builder()
                .symbol(item.getString("symbol"))
                .timestamp(item.getString("timestamp"))
                .open(item.getDouble("open"))
                .high(item.getDouble("high"))
                .low(item.getDouble("low"))
                .close(item.getDouble("close"))
                .volume(item.getDouble("volume"))
                .build();

        if (!item.isNull("ma50")) {
            ohlcvDTO.setMa50(item.getDouble("ma50"));
        }
        if (!item.isNull("ma200")) {
            ohlcvDTO.setMa200(item.getDouble("ma200"));
        }
        if (!item.isNull("diff_ma50_ma200")) {
            ohlcvDTO.setDiff_ma50_ma200(item.getDouble("diff_ma50_ma200"));
        }

        return ohlcvDTO.toEntity();
    }
}
