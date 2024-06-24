package infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.OHLCV;
import com.tradey.technical_analysis.infrastructure.dto.OHLCVDTO;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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

        OHLCV entity = dto.toEntity();

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

//    @Test
//    public void testToEntity_StateDoesNotContainLatestTimestampProcessed() {
//        Map<String, Object> state = new HashMap<>();
//        StateMachineDTO dto = new StateMachineDTO(serviceName, "BTCUSDT", state, "2024-01-01T00:00:00");
//        assertThrows(NoSuchElementException.class, dto::toEntity);
//    }
//
//    @Test
//    public void testFromEntity() {
//        String expectedSymbol = "BTCUSDT";
//        String expectedLatestTimestampProcessed = "2024-01-01T00:00:00";
//        String expectedUpdatedAt = "2024-01-01T00:00:00";
//        TAStateMachineEntity entity = new TAStateMachineEntity(expectedSymbol, expectedUpdatedAt, expectedLatestTimestampProcessed);
//
//        StateMachineDTO dto = StateMachineDTO.fromEntity(entity);
//
//        assertEquals(serviceName, dto.getService());
//        assertEquals(expectedSymbol, dto.getSymbol());
//        assertEquals(expectedUpdatedAt, dto.getUpdatedAt());
//        Map<String, Object> state = dto.getState();
//        assertEquals(expectedLatestTimestampProcessed, state.get("latest_timestamp_processed"));
//    }
}
