package infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PrimaryKey;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoStateMachineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static com.tradey.technical_analysis.pkgs.Constants.serviceName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DynamoStateMachineRepositoryTest {
    private Table table;
    private DynamoStateMachineRepository repository;

    @BeforeEach
    public void setUp() {
        DynamoDB dynamoDB = Mockito.mock(DynamoDB.class);
        table = Mockito.mock(Table.class);
        when(dynamoDB.getTable(Mockito.anyString())).thenReturn(table);
        repository = new DynamoStateMachineRepository(dynamoDB);
    }

    @Test
    public void testGetTAByServiceAndSymbol_ItemExists() {
        String symbol = "BTCUSDT";
        String latestTimestampProcessed = "2024-01-01T00:00:00";

        Map<String, Object> state = new HashMap<>();
        state.put("latest_timestamp_processed", latestTimestampProcessed);

        // Mock
        Item item = new Item()
                .withString("service", serviceName)
                .withString("symbol", symbol)
                .withMap("state", state)
                .withString("updated_at", "2024-01-01T00:00:00");
        when(table.getItem(new PrimaryKey("service", serviceName, "symbol", symbol))).thenReturn(item);

        TAStateMachineEntity entity = repository.getTA(symbol);

        assertEquals(symbol, entity.getSymbol());
        assertEquals(latestTimestampProcessed, entity.getLatestTimestampProcessed());
    }

    @Test
    public void  testGetTAByServiceAndSymbol_ItemDoesNotExist() {
        when(table.getItem(new PrimaryKey("service", serviceName, "symbol", "mock"))).thenReturn(null);

        TAStateMachineEntity entity = repository.getTA("mock");

        assertNull(entity);
    }

    @Test
    public void testUpsertTA() {
        String symbol = "BTCUSDT";
        String latestTimestampProcessed = "2024-01-01T00:00:00";
        String updatedAt = "2024-01-01T00:00:00";
        TAStateMachineEntity entity = new TAStateMachineEntity(symbol, updatedAt, latestTimestampProcessed);

        repository.upsertTA(symbol, entity);

        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        verify(table).putItem(itemCaptor.capture());
        Item item = itemCaptor.getValue();

        assertEquals(serviceName, item.getString("service"));
        assertEquals(symbol, item.getString("symbol"));
        assertEquals(updatedAt, item.getString("updated_at"));
        Map<String, Object> state = item.getMap("state");
        assertEquals(latestTimestampProcessed, state.get("latest_timestamp_processed"));
    }
}
