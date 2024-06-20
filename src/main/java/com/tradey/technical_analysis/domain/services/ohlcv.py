from typing import Optional

from src.domain.entity.ohlcv import OHLCV
from src.domain.interface.repository.ohlcv import IOHLCVRepository


class OHLCVService:
    def __init__(self, ohlcv_repository: IOHLCVRepository):
        self._ohlcv_repository = ohlcv_repository

    async def batch_create(self, list_ohlcv: list[OHLCV]):
        await self._ohlcv_repository.batch_create(list_ohlcv)

    async def get_latest_by_symbol(self, symbol: str) -> Optional[OHLCV]:
        return await self._ohlcv_repository.get_latest_by_symbol(symbol)
