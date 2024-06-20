from decimal import Decimal
from typing import Optional
from pydantic import BaseModel

from src.domain.entity.ohlcv import OHLCV


class DynamoOHLCVDTO(BaseModel):
    symbol: str
    open: Decimal
    high: Decimal
    low: Decimal
    close: Decimal
    volume: Decimal
    timestamp: str

    ma50: Optional[Decimal] = None
    ma200: Optional[Decimal] = None
    diff_ma50_ma200: Optional[Decimal] = None

    def to_model(self) -> OHLCV:
        return OHLCV.model_validate(self.model_dump())
