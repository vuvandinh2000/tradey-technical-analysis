import logging
from datetime import datetime
from typing import Optional, List

from binance.client import AsyncClient as AsyncBinanceClient

from src.domain.interface.client.exchange import IExchangeClient
from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.entity.ohlcv import OHLCV
from src.infra.dto.exchange_info_dto import ExchangeInfoDTO, KLineDTO
from src.pkgs.enum import ExchangeTypeEnum
from src.pkgs.utils import format_datetime_to_unix_ms, format_unix_ms_to_datetime


class BinanceClient(IExchangeClient):
    def __init__(self, client: AsyncBinanceClient):
        self._logger = logging.getLogger(__name__)
        self._logger.setLevel(logging.INFO)
        self._client = client

    async def get_exchange_info(self, exchange_type: ExchangeTypeEnum) -> ExchangeInfo:
        if exchange_type != ExchangeTypeEnum.FUTURES_U_MARGINED:
            raise ValueError(f"Unsupported exchange type: {exchange_type}")
        exchange_info_dict = await self._client.futures_exchange_info()
        exchange_info_dto = ExchangeInfoDTO.model_validate(exchange_info_dict)
        return exchange_info_dto.to_model(
            exchange_type=ExchangeTypeEnum.FUTURES_U_MARGINED,
            created_at=format_datetime_to_unix_ms(datetime.now())
        )

    async def fetch_ohlcv(
        self,
        symbol: str,
        interval: str,
        start_time_ms: int,
        end_time_ms: int,
        limit: int = 500,
    ) -> List[OHLCV]:
        if 500 < limit <= 1000:
            self._logger.warning("⚠️ Fetch with the large number of OHLCV can cause out of memory.")
        elif limit > 1000:
            msg = "❌ Not supported to fetch more than 1000 OHLCVs."
            self._logger.error(msg)
            raise ValueError(msg)

        # Binance limit default 500 klines, max 1000 klines per request
        list_klines: List[List[int | str]] = []

        self._logger.info("🛫 Fetching klines of '{}' from '{}' to '{}'".format(
            symbol,
            format_unix_ms_to_datetime(start_time_ms),
            format_unix_ms_to_datetime(end_time_ms),
        ))

        fetched_klines = await self._client.get_klines(
            symbol=symbol,
            interval=interval,
            startTime=start_time_ms,
            endTime=end_time_ms,
            limit=limit,
        )
        list_klines.extend(fetched_klines)

        list_ohlcv: List[OHLCV] = [
            KLineDTO(
                open_time=klines[0],
                open=klines[1],
                high=klines[2],
                low=klines[3],
                close=klines[4],
                volume=klines[5],
                close_time=klines[6],
                quote_asset_volume=klines[7],
                number_of_trades=klines[8],
                taker_buy_volume=klines[9],
                taker_buy_quote_asset_volume=klines[10],
                ignore=klines[11],
            ).to_model(symbol=symbol)
            for klines in list_klines
        ]

        return list_ohlcv
