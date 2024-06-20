from enum import StrEnum, auto


class CandlestickPatternEnum(StrEnum):
    DOJI = auto()
    GRAVESTONE_DOJI = auto()
    DRAGONFLY_DOJI = auto()
    LONG_LEGGED_DOJI = auto()
    HAMMER = auto()
    INV_HAMMER = auto()
    SPINNING_TOP = auto()
    MARUBOZU = auto()
    ENGULF = auto()
    ENGULFING = auto()
    HARAMI = auto()
    DARK_CLOUD_COVER = auto()
    PIERCING = auto()


class TechnicalIndicatorEnum(StrEnum):
    MA50 = auto()
    MA200 = auto()


class MarketEnum(StrEnum):
    BULL = auto()
    BEAR = auto()
    SIDEWAYS = auto()
    UNKNOWN = auto()


class ExchangeTypeEnum(StrEnum):
    FUTURES_U_MARGINED = "FUTURES-U_MARGINED"
    SPOT = "SPOT"
