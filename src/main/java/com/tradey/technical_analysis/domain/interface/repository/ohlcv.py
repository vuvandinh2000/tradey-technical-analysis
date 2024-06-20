from typing import Optional

from src.domain.entity.ohlcv import OHLCV


class IOHLCVRepository:
    async def batch_create(self, list_ohlcv: list[OHLCV]):
        raise NotImplementedError

    async def get_latest_by_symbol(self, symbol: str) -> Optional[OHLCV]:
        raise NotImplementedError
