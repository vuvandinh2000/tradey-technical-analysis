from typing import List, Dict

from pydantic import BaseModel

from src.domain.entity.symbol_info import SymbolInfo


class DynamoSymbolInfoDTO(BaseModel):
    exchange_type: str
    symbol: str
    server_time: int
    timezone: str
    created_at: int
    pair: str
    contract_type: str
    delivery_date: int
    onboard_date: int
    status: str
    maint_margin_percent: str
    required_margin_percent: str
    base_asset: str
    quote_asset: str
    margin_asset: str
    price_precision: int
    quantity_precision: int
    base_asset_precision: int
    quote_precision: int
    underlying_type: str
    underlying_sub_type: List[str]
    settle_plan: int
    trigger_protect: str
    liquidation_fee: str
    market_take_bound: str
    max_move_order_limit: int
    filters: List[Dict]
    order_types: List[str]
    time_in_force: List[str]

    def to_model(self) -> SymbolInfo:
        return SymbolInfo.model_validate(self.model_dump())
