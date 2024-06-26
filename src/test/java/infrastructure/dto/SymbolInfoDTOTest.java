package infrastructure.dto;

import com.tradey.technical_analysis.domain.entity.SymbolInfoEntity;
import com.tradey.technical_analysis.infrastructure.dto.SymbolInfoDTO;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SymbolInfoDTOTest {
    @Test
    public void testToEntity() {
        String expectedExchangeType = "BINANCE";
        String expectedSymbol = "BTCUSDT";
        BigInteger expectedOnboardDate = new BigInteger("1714633200000");

        SymbolInfoDTO dto = new SymbolInfoDTO(expectedExchangeType, expectedSymbol, expectedOnboardDate);

        SymbolInfoEntity entity = dto.toEntity();

        assertEquals(expectedOnboardDate, entity.getOnboardDate());
    }
}
