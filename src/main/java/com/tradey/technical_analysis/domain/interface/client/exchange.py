from abc import ABC
from typing import Optional, List

from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.entity.ohlcv import OHLCV
from src.pkgs.enum import ExchangeTypeEnum


class IExchangeClient(ABC):
    async def get_exchange_info(self, exchange_type: ExchangeTypeEnum) -> ExchangeInfo:
        raise NotImplementedError

    async def fetch_ohlcv(
        self,
        symbol: str,
        interval: str,
        start_time_ms: int,
        end_time_ms: int,
        limit: Optional[int] = None,
    ) -> List[OHLCV]:
        raise NotImplementedError
