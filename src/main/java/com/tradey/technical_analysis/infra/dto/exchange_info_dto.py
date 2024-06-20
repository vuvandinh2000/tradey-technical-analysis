from __future__ import annotations
from typing import Dict, List, Optional
from pydantic import BaseModel

from src.domain.entity.ohlcv import OHLCV
from src.domain.entity.exchange_info import ExchangeInfo
from src.domain.entity.symbol_info import SymbolInfo
from src.pkgs.enum import ExchangeTypeEnum
from src.pkgs.utils import format_unix_ms_to_datetime


class ExchangeInfoDTO(BaseModel):
    timezone: str
    serverTime: int
    futuresType: Optional[str]
    rateLimits: List[Dict]
    exchangeFilters: List
    assets: List[Dict]
    symbols: List[SymbolInfoDTO]

    def to_model(self, exchange_type: ExchangeTypeEnum, created_at: int) -> ExchangeInfo:
        return ExchangeInfo(
            timezone=self.timezone,
            server_time=self.serverTime,
            futures_type=self.futuresType,
            rate_limits=self.rateLimits,
            exchange_filters=self.exchangeFilters,
            assets=self.assets,
            symbols=[symbol.to_model(self, exchange_type, created_at) for symbol in self.symbols],
        )


class KLineDTO(BaseModel):
    open_time: int
    open: str
    high: str
    low: str
    close: str
    volume: str
    close_time: int
    quote_asset_volume: str
    number_of_trades: int
    taker_buy_volume: str
    taker_buy_quote_asset_volume: str
    ignore: str

    def to_model(self, symbol: str) -> OHLCV:
        return OHLCV(
            symbol=symbol,
            timestamp=format_unix_ms_to_datetime(self.close_time),
            open=float(self.open),
            high=float(self.high),
            low=float(self.low),
            close=float(self.close),
            volume=float(self.volume),
        )


class SymbolInfoDTO(BaseModel):
    symbol: str
    pair: str
    contractType: str
    deliveryDate: int
    onboardDate: int
    status: str
    maintMarginPercent: str
    requiredMarginPercent: str
    baseAsset: str
    quoteAsset: str
    marginAsset: str
    pricePrecision: int
    quantityPrecision: int
    baseAssetPrecision: int
    quotePrecision: int
    underlyingType: str
    underlyingSubType: List[str]
    settlePlan: int
    triggerProtect: str
    liquidationFee: str
    marketTakeBound: str
    maxMoveOrderLimit: int
    filters: List[Dict]
    orderTypes: List[str]
    timeInForce: List[str]

    def to_model(
            self,
            parent: ExchangeInfoDTO,
            exchange_type: ExchangeTypeEnum,
            created_at: int,
    ) -> SymbolInfo:
        return SymbolInfo(
            exchange_type=exchange_type,
            symbol=self.symbol,
            server_time=parent.serverTime,
            timezone=parent.timezone,
            created_at=created_at,
            pair=self.pair,
            contract_type=self.contractType,
            delivery_date=self.deliveryDate,
            onboard_date=self.onboardDate,
            status=self.status,
            maint_margin_percent=self.maintMarginPercent,
            required_margin_percent=self.requiredMarginPercent,
            base_asset=self.baseAsset,
            quote_asset=self.quoteAsset,
            margin_asset=self.marginAsset,
            price_precision=self.pricePrecision,
            quantity_precision=self.quantityPrecision,
            base_asset_precision=self.baseAssetPrecision,
            quote_precision=self.quotePrecision,
            underlying_type=self.underlyingType,
            underlying_sub_type=self.underlyingSubType,
            settle_plan=self.settlePlan,
            trigger_protect=self.triggerProtect,
            liquidation_fee=self.liquidationFee,
            market_take_bound=self.marketTakeBound,
            max_move_order_limit=self.maxMoveOrderLimit,
            filters=self.filters,
            order_types=self.orderTypes,
            time_in_force=self.timeInForce,
        )
