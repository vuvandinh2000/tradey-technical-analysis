import asyncio
import json
import logging
from decimal import Decimal
from boto3.dynamodb.conditions import Key

from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.entity.symbol_info import SymbolInfo
from src.domain.interface.repository.symbol_info import ISymbolInfoRepository
from src.infra.persistence.dynamo.dto.symbol_info import DynamoSymbolInfoDTO
from src.pkgs.enum import ExchangeTypeEnum


class DynamoSymbolInfoRepository(ISymbolInfoRepository):
    __table_name__ = "symbol_info"

    def __init__(self, dynamodb_client):
        self._logger = logging.getLogger(__name__)
        self._logger.setLevel(logging.INFO)
        self._table = dynamodb_client.Table(self.__table_name__)

    async def create(self, exchange_info: ExchangeInfo):
        try:
            task_create = []
            for symbol_info in exchange_info.symbols:
                # https://github.com/boto/boto3/issues/665
                symbol_info_dict = json.loads(json.dumps(symbol_info.model_dump()), parse_float=Decimal)
                task_create.append(self._table.put_item(Item=symbol_info_dict))
            return await asyncio.gather(*task_create)
        except Exception as e:
            msg = f"Failed to put SymbolInfo: {e}"
            self._logger.error(msg)
            raise Exception(msg)

    async def get_by_exchange_type_and_symbol(self, exchange_type: ExchangeTypeEnum, symbol: str) -> SymbolInfo | None:
        try:
            symbol_info_dict = self._table.query(
                KeyConditionExpression=Key("exchange_type").eq(exchange_type) & Key("symbol").eq(symbol),
                Limit=1,
            )
            if symbol_info_dict["Count"] <= 0:
                return None
            return DynamoSymbolInfoDTO.model_validate(symbol_info_dict["Items"][0]).to_model()
        except Exception as e:
            msg = f"Failed to get SymbolInfo by exchange_type={exchange_type} & symbol={symbol}: {e}"
            self._logger.error(msg)
            raise Exception(msg)
