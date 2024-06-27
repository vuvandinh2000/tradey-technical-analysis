package infrastructure.repositories.dynamo;

import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.infrastructure.repositories.dynamo.DynamoOHLCVRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DynamoOHLCVRepositoryTest {
    private Table table;
    private DynamoOHLCVRepository repository;

    @BeforeEach
    public void setUp() {
        DynamoDB dynamoDB = Mockito.mock(DynamoDB.class);
        table = Mockito.mock(Table.class);
        when(dynamoDB.getTable(Mockito.anyString())).thenReturn(table);
        repository = new DynamoOHLCVRepository(dynamoDB);
    }

    @Test
    public void testGetBySymbolAndTimestamp_ItemExists() {
        String expectedSymbol = "BTCUSDT";
        String expectedTimestamp = "2024-01-01T00:00:00";
        double expectedOpen = 8379.62;
        double expectedHigh = 8427.52;
        double expectedLow = 8365.81;
        double expectedClose = 8409.28;
        double expectedVolume = 1338.356337;
        double expectedMa50 = 1234.5;
        double expectedMa200 = 2345.6;
        double expectedDiffMa50Ma200 = 123.4;

        // Mock
        Item item = new Item()
                .withString("symbol", expectedSymbol)
                .withString("timestamp", expectedTimestamp)
                .withDouble("open", expectedOpen)
                .withDouble("high", expectedHigh)
                .withDouble("low", expectedLow)
                .withDouble("close", expectedClose)
                .withDouble("volume", expectedVolume)
                .withDouble("ma50", expectedMa50)
                .withDouble("ma200", expectedMa200)
                .withDouble("diff_ma50_ma200", expectedDiffMa50Ma200);
        when(table.getItem(new PrimaryKey("symbol", expectedSymbol, "timestamp", expectedTimestamp))).thenReturn(item);

        OHLCVEntity entity = repository.getBySymbolAndTimestamp(expectedSymbol, expectedTimestamp);

        assertEquals(expectedSymbol, entity.getSymbol());
        assertEquals(expectedTimestamp, entity.getTimestamp());
        assertEquals(expectedOpen, entity.getOpen());
        assertEquals(expectedHigh, entity.getHigh());
        assertEquals(expectedLow, entity.getLow());
        assertEquals(expectedClose, entity.getClose());
        assertEquals(expectedVolume, entity.getVolume());
        assertEquals(expectedMa50, entity.getMa50());
        assertEquals(expectedMa200, entity.getMa200());
        assertEquals(expectedDiffMa50Ma200, entity.getDiffMa50Ma200());
    }

    @Test
    public void testGetBySymbolAndTimestamp_ItemExists_TAMetricsAreNull() {
        String expectedSymbol = "BTCUSDT";
        String expectedTimestamp = "2024-01-01T00:00:00";
        double expectedOpen = 8379.62;
        double expectedHigh = 8427.52;
        double expectedLow = 8365.81;
        double expectedClose = 8409.28;
        double expectedVolume = 1338.356337;

        // Mock
        Item item = createTestOHLCVItem(expectedSymbol, expectedTimestamp, expectedOpen, expectedHigh, expectedLow, expectedClose, expectedVolume);
        when(table.getItem(new PrimaryKey("symbol", expectedSymbol, "timestamp", expectedTimestamp))).thenReturn(item);

        OHLCVEntity entity = repository.getBySymbolAndTimestamp(expectedSymbol, expectedTimestamp);

        assertEquals(expectedSymbol, entity.getSymbol());
        assertEquals(expectedTimestamp, entity.getTimestamp());
        assertEquals(expectedOpen, entity.getOpen());
        assertEquals(expectedHigh, entity.getHigh());
        assertEquals(expectedLow, entity.getLow());
        assertEquals(expectedClose, entity.getClose());
        assertEquals(expectedVolume, entity.getVolume());
        assertNull(entity.getMa50());
        assertNull(entity.getMa200());
        assertNull(entity.getDiffMa50Ma200());
    }

    @Test
    public void  testGetBySymbolAndTimestamp_ItemDoesNotExist() {
        when(table.getItem(new PrimaryKey("symbol", "mockSymbol", "mockTimestamp", "mock"))).thenReturn(null);

        OHLCVEntity entity = repository.getBySymbolAndTimestamp("mockSymbol", "mockTimestamp");

        assertNull(entity);
    }

    @Test
    public void testGetAllBySymbolOlderThanTimestamp() {
        // Mock
        String symbol = "BTCUSDT";
        String expectedTimestamp1 = "2024-01-01T00:00:00";
        String expectedTimestamp2 = "2024-01-01T00:00:01";
        double expectedOpen1 = 123;
        double expectedOpen2 = 234;
        double expectedHigh1 = 456;
        double expectedHigh2 = 678;
        double expectedLow1 = 890;
        double expectedLow2 = 111;
        double expectedClose1 = 222;
        double expectedClose2 = 333;
        double expectedVolume1 = 444;
        double expectedVolume2 = 555;

        Item item1 = createTestOHLCVItem(symbol, expectedTimestamp1, expectedOpen1, expectedHigh1, expectedLow1, expectedClose1, expectedVolume1);
        Item item2 = createTestOHLCVItem(symbol, expectedTimestamp2, expectedOpen2, expectedHigh2, expectedLow2, expectedClose2, expectedVolume2);

        ItemCollection<QueryOutcome> items = mock(ItemCollection.class);
        IteratorSupport<Item, QueryOutcome> mockIterator = mock(IteratorSupport.class);
        when(mockIterator.hasNext()).thenReturn(true, true, false);
        when(mockIterator.next()).thenReturn(item1, item2);
        when(items.iterator()).thenReturn(mockIterator);
        when(table.query(any(QuerySpec.class))).thenReturn(items);

        List<OHLCVEntity> result = repository.getAllBySymbolOlderEqualThanTimestamp("mock", "mock", 200);

        assertEquals(2, result.size());
        assertEquals(symbol, result.get(0).getSymbol());
        assertEquals(expectedTimestamp1, result.get(0).getTimestamp());
        assertEquals(symbol, result.get(1).getSymbol());
        assertEquals(expectedTimestamp2, result.get(1).getTimestamp());
    }

    private Item createTestOHLCVItem(String symbol, String timestamp, double open, double high, double low, double close, double volume) {
        return new Item()
                .withString("symbol", symbol)
                .withString("timestamp", timestamp)
                .withDouble("open", open)
                .withDouble("high", high)
                .withDouble("low", low)
                .withDouble("close", close)
                .withDouble("volume", volume)
                .withNull("ma50")
                .withNull("ma200")
                .withNull("diff_ma50_ma200");
    }
}
