package com.tradey.technical_analysis.infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.tradey.technical_analysis.domain.entity.OHLCV;
import com.tradey.technical_analysis.domain.repositories.OHLCVRepository;
import com.tradey.technical_analysis.infrastructure.dto.OHLCVDTO;

import java.util.ArrayList;
import java.util.List;

import static com.tradey.technical_analysis.pkgs.Constants.ohlcvTableName;

public class DynamoOHLCVRepository implements OHLCVRepository {

    private final Table table;

    public DynamoOHLCVRepository(DynamoDB dynamoDB) {
        this.table = dynamoDB.getTable(ohlcvTableName);
    }

    @Override
    public OHLCV getBySymbolAndTimestamp(String symbol, String timestamp) {
        Item item = table.getItem(new PrimaryKey("symbol", symbol, "timestamp", timestamp));
        return OHLCVItemToEntity(item);
    }

    @Override
    public List<OHLCV> getAllBySymbolOlderThanTimestamp(String symbol, String timestamp, int limit) {
        RangeKeyCondition rangeKeyCondition = new RangeKeyCondition("timestamp").lt(timestamp);
        QuerySpec querySpec = new QuerySpec()
                .withHashKey("symbol", symbol)
                .withRangeKeyCondition(rangeKeyCondition)
                .withMaxResultSize(limit);
        Iterable<Item> items = this.table.query(querySpec);

        List<OHLCV> result = new ArrayList<>();
        for (Item item: items) {
            result.add(OHLCVItemToEntity(item));
        }

        return result;
    }

    private OHLCV OHLCVItemToEntity(Item item) {
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
