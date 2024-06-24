package com.tradey.technical_analysis.infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.tradey.technical_analysis.domain.entity.OHLCV;
import com.tradey.technical_analysis.domain.repositories.OHLCVRepository;
import com.tradey.technical_analysis.infrastructure.dto.OHLCVDTO;

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

        if (item == null) {
            return null;
        }

        OHLCVDTO ohlcvDTO = OHLCVDTO.builder()
                .symbol(item.getString("symbol"))
                .timestamp(item.getString("timestamp"))
                .open(item.getFloat("open"))
                .high(item.getFloat("high"))
                .low(item.getFloat("low"))
                .close(item.getFloat("close"))
                .volume(item.getFloat("volume"))
                .build();

        if (item.isPresent("ma50")) {
            ohlcvDTO.setMa50(item.getDouble("ma50"));
        }
        if (item.isPresent("ma200")) {
            ohlcvDTO.setMa200(item.getDouble("ma200"));
        }
        if (item.isPresent("diff_ma50_ma200")) {
            ohlcvDTO.setDiff_ma50_ma200(item.getDouble("diff_ma50_ma200"));
        }

        return ohlcvDTO.toEntity();
    }

    @Override
    public List<OHLCV> getAllBySymbolOlderThanTimestamp(String symbol, String timestamp) {
        return null;
    }
}
