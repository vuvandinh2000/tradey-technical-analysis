import json
import logging
import asyncio
from decimal import Decimal
from typing import Optional
from boto3.dynamodb.conditions import Key

from src.domain.interface.repository.ohlcv import IOHLCVRepository
from src.domain.entity.ohlcv import OHLCV


class DynamoOHLCVRepository(IOHLCVRepository):
    __table_name__ = "ohlcv_future"

    def __init__(self, dynamodb_client):
        self._logger = logging.getLogger(__name__)
        self._logger.setLevel(logging.INFO)
        self._table = dynamodb_client.Table(self.__table_name__)

    async def create(self, ohlcv: OHLCV):
        try:
            # https://github.com/boto/boto3/issues/665
            ohlcv_dict = json.loads(json.dumps(ohlcv.to_serializable_dict()), parse_float=Decimal)
            self._table.put_item(Item=ohlcv_dict)
        except Exception as e:
            msg = f"Failed to put OHLCV: {e}"
            self._logger.error(msg)
            raise Exception(msg)

    async def batch_create(self, list_ohlcv: list[OHLCV]):
        async with asyncio.TaskGroup() as tg:
            for ohlcv in list_ohlcv:
                tg.create_task(self.create(ohlcv))
        self._logger.info(f"✈️ Created batch {len(list_ohlcv)} OHLCVs.")

    async def get_latest_by_symbol(self, symbol: str) -> Optional[OHLCV]:
        try:
            ohlcv_dict = self._table.query(
                KeyConditionExpression=Key("symbol").eq(symbol),
                ScanIndexForward=False,  # Descending order
                Limit=1,
            )
            if ohlcv_dict["Count"] <= 0:
                return None
            return OHLCV.model_validate(ohlcv_dict["Items"][0])
        except Exception as e:
            msg = f"Failed to get latest OHLCV by symbol '{symbol}': {e}"
            self._logger.error(msg)
            raise Exception(msg)
