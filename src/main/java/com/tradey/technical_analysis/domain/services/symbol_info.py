from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.entity.symbol_info import SymbolInfo
from src.domain.interface.repository.symbol_info import ISymbolInfoRepository
from src.pkgs.enum import ExchangeTypeEnum


class SymbolInfoService:
    def __init__(self, symbol_info_repository: ISymbolInfoRepository):
        self._symbol_info_repository = symbol_info_repository

    async def create(self, exchange_info: ExchangeInfo):
        """
        Create symbol info of all exchange.
        """
        return await self._symbol_info_repository.create(exchange_info)

    async def get_by_exchange_type_and_symbol(self, exchange_type: ExchangeTypeEnum, symbol: str) -> SymbolInfo | None:
        return await self._symbol_info_repository.get_by_exchange_type_and_symbol(exchange_type, symbol)
