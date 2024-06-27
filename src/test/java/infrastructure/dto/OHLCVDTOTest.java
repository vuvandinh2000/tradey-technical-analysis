package infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.OHLCVEntity;
import com.tradey.technical_analysis.infrastructure.dto.OHLCVDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OHLCVDTOTest {
    @Test
    public void testToEntity_TAMetricsAreNull() {
        String expectedSymbol = "BTCUSDT";
        String expectedTimestamp = "2024-01-01T00:00:00";
        double expectedOpen = 8379.62;
        double expectedHigh = 8427.52;
        double expectedLow = 8365.81;
        double expectedClose = 8409.28;
        double expectedVolume = 1338.356337;

        OHLCVDTO dto = OHLCVDTO.builder()
                .symbol(expectedSymbol)
                .timestamp(expectedTimestamp)
                .open(expectedOpen)
                .high(expectedHigh)
                .low(expectedLow)
                .close(expectedClose)
                .volume(expectedVolume)
                .build();

        OHLCVEntity entity = dto.toEntity();

        assertEquals(expectedSymbol, entity.getSymbol());
        assertEquals(expectedTimestamp, entity.getTimestamp());
        assertEquals(expectedOpen, entity.getOpen());
        assertEquals(expectedHigh, entity.getHigh());
        assertEquals(expectedLow, entity.getLow());
        assertEquals(expectedClose, entity.getClose());
        assertEquals(expectedVolume, entity.getVolume());
        assertEquals(expectedVolume, entity.getVolume());
        assertNull(entity.getMa50());
        assertNull(entity.getMa200());
        assertNull(entity.getDiffMa50Ma200());
    }

    @Test
    public void testToEntity_TAMetricsNotNull() {
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

        OHLCVDTO dto = OHLCVDTO.builder()
                .symbol(expectedSymbol)
                .timestamp(expectedTimestamp)
                .open(expectedOpen)
                .high(expectedHigh)
                .low(expectedLow)
                .close(expectedClose)
                .volume(expectedVolume)
                .ma50(expectedMa50)
                .ma200(expectedMa200)
                .diff_ma50_ma200(expectedDiffMa50Ma200)
                .build();

        OHLCVEntity entity = dto.toEntity();

        assertEquals(expectedSymbol, entity.getSymbol());
        assertEquals(expectedTimestamp, entity.getTimestamp());
        assertEquals(expectedOpen, entity.getOpen());
        assertEquals(expectedHigh, entity.getHigh());
        assertEquals(expectedLow, entity.getLow());
        assertEquals(expectedClose, entity.getClose());
        assertEquals(expectedVolume, entity.getVolume());
        assertEquals(expectedVolume, entity.getVolume());
        assertEquals(expectedMa50, entity.getMa50());
        assertEquals(expectedMa200, entity.getMa200());
        assertEquals(expectedDiffMa50Ma200, entity.getDiffMa50Ma200());
    }
}
