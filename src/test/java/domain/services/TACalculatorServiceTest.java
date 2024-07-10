package domain.services;

import com.tradey.technical_analysis.domain.entity.KLineEntity;
import com.tradey.technical_analysis.domain.services.TACalculatorService;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TACalculatorServiceTest {
    @Test
    public void testCalculateMovingAverage() {
        TACalculatorService taCalculatorService = new TACalculatorService();
        double ohlcvClose1 = 123.4;
        double ohlcvClose2 = 234.5;
        double ohlcvClose3 = 345.6;
        double ohlcvClose4 = 456.7;

        KLineEntity ohlcv1 = createOHLCVEntity(ohlcvClose1);
        KLineEntity ohlcv2 = createOHLCVEntity(ohlcvClose2);
        KLineEntity ohlcv3 = createOHLCVEntity(ohlcvClose3);
        KLineEntity ohlcv4 = createOHLCVEntity(ohlcvClose4);

        Double ma3 = taCalculatorService.calculateMovingAverage(Arrays.asList(ohlcv1, ohlcv2, ohlcv3, ohlcv4), 3);

        assertEquals((ohlcvClose2+ohlcvClose3+ohlcvClose4)/3, ma3);

        Double ma = taCalculatorService.calculateMovingAverage(Arrays.asList(ohlcv1, ohlcv2, ohlcv3, ohlcv4), 5);

        assertNull(ma);

        Double ma0 = taCalculatorService.calculateMovingAverage(Arrays.asList(ohlcv1, ohlcv2, ohlcv3, ohlcv4), 0);

        assertNull(ma0);
    }

    private KLineEntity createOHLCVEntity(double close) {
        return KLineEntity.builder()
                .open(0).high(0).low(0).close(close)
                .ma200(null).ma50(null).diffMa50Ma200(null)
                .build();
    }
}
