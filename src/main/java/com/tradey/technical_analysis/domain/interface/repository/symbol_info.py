from abc import ABC

from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.entity.symbol_info import SymbolInfo
from src.pkgs.enum import ExchangeTypeEnum


class ISymbolInfoRepository(ABC):
    async def create(self, exchange_info: ExchangeInfo):
        raise NotImplementedError

    async def get_by_exchange_type_and_symbol(self, exchange_type: ExchangeTypeEnum, symbol: str) -> SymbolInfo | None:
        raise NotImplementedError
