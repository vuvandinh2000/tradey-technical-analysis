import logging
from datetime import datetime, timedelta, timezone

from src.domain.services.ohlcv import OHLCVService
from src.domain.services.technical_analysis import TechnicalAnalysisService
from src.pkgs.enum import ExchangeTypeEnum
from src.pkgs.utils import format_datetime_to_unix_ms, format_unix_ms_to_datetime


class TechnicalAnalysisUseCase:
    def __init__(
            self,
            ohlcv_service: OHLCVService,
            technical_analysis_service: TechnicalAnalysisService,
    ):
        self._logger = logging.getLogger(__name__)
        self._logger.setLevel(logging.INFO)
        self._ohlcv_service = ohlcv_service
        self._exchange_service = exchange_service
        self._ohlcv_service = ohlcv_service

    async def execute(
            self,
            exchange_type: ExchangeTypeEnum = ExchangeTypeEnum.FUTURES_U_MARGINED,
            symbol: str = "BTCUSDT",
            interval: str = "1h",
    ):
        symbol_info = await self._symbol_info_service.get_by_exchange_type_and_symbol(
            exchange_type=exchange_type,
            symbol=symbol,
        )
        if symbol_info is None:
            exchange_info = await self._exchange_service.get_exchange_info(exchange_type)
            await self._symbol_info_service.create(exchange_info)
            symbol_info = await self._symbol_info_service.get_by_exchange_type_and_symbol(
                exchange_type=exchange_type,
                symbol=symbol,
            )
            if symbol_info is None:
                raise Exception(f"Failed to get SymbolInfo for exchange_type='{exchange_type}' and symbol='{symbol}'")
        latest_ohlcv = await self._ohlcv_service.get_latest_by_symbol(symbol=symbol)

        if latest_ohlcv is not None:
            start_time_ms = format_datetime_to_unix_ms(latest_ohlcv.timestamp)
        else:
            start_time_ms = format_datetime_to_unix_ms(
                format_unix_ms_to_datetime(symbol_info.onboard_date) + 1 * timedelta(days=1)
            )
        end_time_ms = format_datetime_to_unix_ms(datetime.now(timezone.utc))

        while True:
            # Fetch and put to DB max 100 items per batch to prevent memory overflow
            list_new_ohlcv = await self._exchange_service.fetch_ohlcv(
                symbol=symbol,
                interval=interval,
                start_time_ms=start_time_ms,
                end_time_ms=end_time_ms,
                limit=100,
            )

            if len(list_new_ohlcv) <= 0:
                self._logger.info(f"🔔 No new OHLCV data to fetch for symbol='{symbol}'")
                return

            await self._ohlcv_service.batch_create(list_new_ohlcv)

            if len(list_new_ohlcv) < 100:
                # Fetch less than 100 items means the last batch
                self._logger.info(f"✅ The last batch was fetched with {len(list_new_ohlcv)} OHLCVs.")
                break
            else:
                self._logger.info("✅️ Fetched {} new OHLCVs of '{}' from '{}' to '{}'".format(
                    len(list_new_ohlcv),
                    symbol,
                    format_unix_ms_to_datetime(start_time_ms),
                    list_new_ohlcv[-1].timestamp,
                ))

            # The next batch start time is the last timestamp of the current batch
            start_time_ms = format_datetime_to_unix_ms(list_new_ohlcv[-1].timestamp)
