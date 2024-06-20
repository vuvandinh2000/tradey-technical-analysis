from pydantic import BaseModel


class SymbolInfo(BaseModel):
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
    underlying_sub_type: list[str]
    settle_plan: int
    trigger_protect: str
    liquidation_fee: str
    market_take_bound: str
    max_move_order_limit: int
    filters: list[dict]
    order_types: list[str]
    time_in_force: list[str]
