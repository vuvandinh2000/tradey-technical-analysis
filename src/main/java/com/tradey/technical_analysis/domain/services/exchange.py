from typing import List, Optional

from src.domain.entity.ohlcv import OHLCV
from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.interface.client.exchange import IExchangeClient
from src.pkgs.enum import ExchangeTypeEnum


class ExchangeService:
    """
    Adapter to the exchange client.
    """
    def __init__(self, exchange_client: IExchangeClient):
        self._exchange_client = exchange_client

    async def get_exchange_info(self, exchange_type: ExchangeTypeEnum) -> ExchangeInfo:
        return await self._exchange_client.get_exchange_info(exchange_type)

    async def fetch_ohlcv(
        self,
        symbol: str,
        interval: str,
        start_time_ms: int,
        end_time_ms: int,
        limit: Optional[int],
    ) -> List[OHLCV]:
        return await self._exchange_client.fetch_ohlcv(
            symbol=symbol,
            interval=interval,
            start_time_ms=start_time_ms,
            end_time_ms=end_time_ms,
            limit=limit,
        )
