import os
import json
import logging

from src.domain.entity.symbol_info import SymbolInfo
from src.domain.entity.exchange_info import ExchangeInfo
from src.infra.dto.exchange_info_dto import ExchangeInfoDTO
from src.domain.interface.repository.symbol_info import ISymbolInfoRepository
from src.pkgs.enum import ExchangeTypeEnum
from src.pkgs.utils import format_unix_ms_to_datetime


class FileSymbolInfoRepository(ISymbolInfoRepository):
    __filename__ = "exchange_info_futures.json"

    def __init__(self):
        self._logger = logging.getLogger(__name__)
        self._logger.setLevel(logging.INFO)
        self._filename = self.__filename__

    async def create(self, exchange_info: ExchangeInfo):
        try:
            self._logger.info(f"Saving {len(exchange_info.symbols)} symbols to {self._filename}")
            with open(self._filename, "w") as f:
                json.dump(exchange_info.model_dump(), f, indent=4)
        except Exception as e:
            msg = f"Failed to create ExchangeInfo: {e}"
            self._logger.error(msg)
            raise Exception(msg)

    async def get_by_exchange_type_and_symbol(self, exchange_type: ExchangeTypeEnum, symbol: str) -> SymbolInfo | None:
        if not os.path.exists(self._filename):
            msg = f"Failed to get ExchangeInfo: file not found '{self._filename}'"
            self._logger.error(msg)
            raise Exception(msg)

        with open(self._filename, "r") as f:
            exchange_info_dto_dict = json.load(f)
            exchange_info_dto = ExchangeInfoDTO.model_validate(exchange_info_dto_dict)
            info_time = format_unix_ms_to_datetime(exchange_info_dto.serverTime)
            self._logger.info(f"Using cached file {self._filename} retrieved at {info_time}")
            return exchange_info_dto.to_model().get_symbol_info(symbol=symbol)
