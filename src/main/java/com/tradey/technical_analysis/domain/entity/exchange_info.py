from typing import Dict, List
from pydantic import BaseModel

from src.domain.entity.symbol_info import SymbolInfo


class ExchangeInfo(BaseModel):
    timezone: str
    server_time: int
    futures_type: str
    rate_limits: List[Dict]
    exchange_filters: List
    assets: List[Dict]
    symbols: List[SymbolInfo]

    def get_symbol_info(self, symbol: str) -> SymbolInfo:
        for sb in self.symbols:
            if sb.symbol == symbol:
                return sb
