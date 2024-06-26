package infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.TAStateMachineEntity;
import com.tradey.technical_analysis.infrastructure.dto.StateMachineDTO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static com.tradey.technical_analysis.pkgs.Constants.serviceName;

public class StateMachineDTOTest {
    @Test
    public void testToEntity_StateContainsLatestTimestampProcessed() {
        String expectedSymbol = "BTCUSDT";
        String expectedLatestTimestampProcessed = "2024-01-01T00:00:00";
        Map<String, Object> state = new HashMap<>();
        state.put("latest_timestamp_processed", expectedLatestTimestampProcessed);

        StateMachineDTO dto = new StateMachineDTO(serviceName, expectedSymbol, state, "2024-01-01T00:00:00");

        TAStateMachineEntity entity = dto.toEntity();

        assertEquals(expectedSymbol, entity.getSymbol());
        assertEquals(expectedLatestTimestampProcessed, entity.getLatestTimestampProcessed());
    }

    @Test
    public void testToEntity_StateDoesNotContainLatestTimestampProcessed() {
        Map<String, Object> state = new HashMap<>();
        StateMachineDTO dto = new StateMachineDTO(serviceName, "BTCUSDT", state, "2024-01-01T00:00:00");
        assertThrows(NoSuchElementException.class, dto::toEntity);
    }

    @Test
    public void testFromEntity() {
        String expectedSymbol = "BTCUSDT";
        String expectedLatestTimestampProcessed = "2024-01-01T00:00:00";
        String expectedUpdatedAt = "2024-01-01T00:00:00";
        TAStateMachineEntity entity = new TAStateMachineEntity(expectedSymbol, expectedUpdatedAt, expectedLatestTimestampProcessed);

        StateMachineDTO dto = StateMachineDTO.fromEntity(entity);

        assertEquals(serviceName, dto.getService());
        assertEquals(expectedSymbol, dto.getSymbol());
        assertEquals(expectedUpdatedAt, dto.getUpdated_at());
        Map<String, Object> state = dto.getState();
        assertEquals(expectedLatestTimestampProcessed, state.get("latest_timestamp_processed"));
    }
}
