import os
import boto3
from typing import Any, Self
from dataclasses import dataclass
from binance.client import AsyncClient as AsyncBinanceClient

from src.config import Config
from src.domain.interface.client.exchange import IExchangeClient
from src.domain.interface.repository.ohlcv import IOHLCVRepository
from src.domain.interface.repository.symbol_info import ISymbolInfoRepository
from src.domain.services.exchange import ExchangeService
from src.domain.services.ohlcv import OHLCVService
from src.domain.services.symbol_info import SymbolInfoService
from src.application.use_case.technical_analysis import TechnicalAnalysisUseCase
from src.infra.api_client.binance import BinanceClient
from src.infra.persistence.dynamo.ohlcv import DynamoOHLCVRepository
from src.infra.persistence.dynamo.symbol_info import DynamoSymbolInfoRepository


@dataclass
class Client:
    dynamodb: Any
    exchange: IExchangeClient


@dataclass
class Repository:
    symbol_info: ISymbolInfoRepository
    ohlcv: IOHLCVRepository


@dataclass
class Service:
    exchange: ExchangeService
    symbol_info: SymbolInfoService
    ohlcv: OHLCVService


@dataclass
class UseCase:
    technical_analysis: TechnicalAnalysisUseCase


@dataclass
class Container:
    use_case: UseCase = None

    @classmethod
    async def create(cls) -> Self:
        self = cls()
        self.config = Config(
            BINANCE_API_KEY=os.environ.get("BINANCE_API_KEY", ""),
            BINANCE_API_SECRET=os.environ.get("BINANCE_API_SECRET", ""),
            BINANCE_API_BASE_URL=os.environ.get("BINANCE_API_BASE_URL", ""),
            BINANCE_API_NEW_ORDER=os.environ.get("BINANCE_API_NEW_ORDER", ""),
            WEBSOCKET_BINANCE_BASE_URL=os.environ.get("BINANCE_WEBSOCKET_BASE_URL", ""),
            WEBSOCKET_BINANCE_NEW_ORDER=os.environ.get("BINANCE_WEBSOCKET_NEW_ORDER", "")
        )

        # Client Singleton
        __exchange_client = await AsyncBinanceClient.create(
            api_key=self.config.BINANCE_API_KEY.get_secret_value(),
            api_secret=self.config.BINANCE_API_SECRET.get_secret_value(),
        )
        self.client = Client(
            dynamodb=boto3.resource("dynamodb"),
            exchange=BinanceClient(client=__exchange_client),
        )

        self.repository = Repository(
            symbol_info=DynamoSymbolInfoRepository(dynamodb_client=self.client.dynamodb),
            ohlcv=DynamoOHLCVRepository(dynamodb_client=self.client.dynamodb),
        )

        self.service = Service(
            exchange=ExchangeService(exchange_client=self.client.exchange),
            symbol_info=SymbolInfoService(symbol_info_repository=self.repository.symbol_info),
            ohlcv=OHLCVService(ohlcv_repository=self.repository.ohlcv),
        )

        self.use_case = UseCase(
            market_info=MarketInfoUseCase(
                symbol_info_service=self.service.symbol_info,
                exchange_service=self.service.exchange,
                ohlcv_service=self.service.ohlcv,
            )
        )

        return self
