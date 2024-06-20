from __future__ import annotations
import logging
from datetime import datetime
from typing import Dict, List, Optional

import numpy as np
import pandas as pd
from pydantic import BaseModel, PrivateAttr

from src.pkgs.enum import CandlestickPatternEnum, MarketEnum, TechnicalIndicatorEnum


class OHLCV(BaseModel):
    symbol: str
    open: float
    high: float
    low: float
    close: float
    volume: float
    timestamp: datetime

    ma50: Optional[float] = None
    ma200: Optional[float] = None
    diff_ma50_ma200: Optional[float] = None

    def to_serializable_dict(self) -> Dict:
        ohlcv_dict = self.model_dump()
        ohlcv_dict["timestamp"] = ohlcv_dict["timestamp"].isoformat()
        return ohlcv_dict


class OHLCVs(BaseModel):
    data: List[OHLCV]
    _dataframe: pd.DataFrame = PrivateAttr()

    def __init__(self, **data):
        super().__init__(**data)
        self._logger = logging.getLogger(__name__)
        self._logger.setLevel(logging.INFO)

        list_dict_ohlcv = [ohlcv.model_dump() for ohlcv in self.data]
        self._dataframe = pd.DataFrame(list_dict_ohlcv)
        self._dataframe.set_index("timestamp", inplace=True)

        self._dataframe_patterns: Dict[CandlestickPatternEnum, pd.DataFrame] = {}
        self._dataframe_indicators: Dict[TechnicalIndicatorEnum, pd.DataFrame] = {}

    def dataframe(self) -> pd.DataFrame:
        return self._dataframe

    def __len__(self):
        return len(self.data)

    def validate_data(self):
        """
        Check for missing candles in the historical OHLC data.
        This function checks for missing candles and duplicate candles in the provided data.
        """
        self._logger.info("-------")
        self._logger.info("Checking Candles:")
        self._logger.info("-------")
        self._logger.info(f"Start: {self.data[0].timestamp}")
        self._logger.info(f"End: {self.data[-1].timestamp}")
        self._logger.info("-------")

        diff = (self.data[1].timestamp - self.data[0].timestamp).total_seconds()

        self._logger.info(f"Interval: {diff}s")
        self._logger.info("-------")

        count = 0
        rows = len(self.data)
        # prev_current_date = None

        for index in range(0, rows - 1):
            current_date = self.data[index].timestamp
            next_date = self.data[index + 1].timestamp

            diff2 = (next_date - current_date).total_seconds()
            if diff2 != diff:
                count += abs((diff2 - diff) / diff)

                # logger.info(current_date)
                # logger.info(next_date)
                # logger.info(f"Missing Candles: {(diff2-diff)/diff} Total: {count}")

                # if(prev_current_date != None):
                #     logger.info(f"Last Missing Candle Interval: {current_date-prev_current_date}")

                # logger.info("------------")

                # prev_current_date = current_date
            elif diff2 <= 0:
                self._logger.info(f"Duplicate Candle: {current_date}")

        self._logger.info(f"Total Missing Candles = {count}")
        self._logger.info("-------")

    def ma(self, period: int):
        period_indicator_map = {
            50: TechnicalIndicatorEnum.MA50,
            200: TechnicalIndicatorEnum.MA200,
        }
        indicator = period_indicator_map.get(period)
        if indicator is None:
            raise ValueError(f"Period {period} is not supported")

        # Get from cache if already calculated
        if self._dataframe_indicators.get(indicator) is not None:
            return self._dataframe_indicators[indicator]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[indicator.value] = df["close"].rolling(window=period).mean()
        self._dataframe_indicators[indicator] = df
        return df

    def doji(self):
        pattern = CandlestickPatternEnum.DOJI
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            np.multiply((df.high - df.low), 0.05) > np.absolute(df.open - df.close), True, False
        )
        self._dataframe_patterns[pattern] = df
        return df

    def gravestone_doji(self):
        pattern = CandlestickPatternEnum.GRAVESTONE_DOJI
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[CandlestickPatternEnum.GRAVESTONE_DOJI.value] = np.where(
            (np.multiply((df.high - df.low), 0.05) > np.absolute(df.open - df.close))
            & (df.open < (df.low + np.multiply((df.high - df.low), 0.05))),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def dragonfly_doji(self):
        pattern = CandlestickPatternEnum.DRAGONFLY_DOJI
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            (np.multiply((df.high - df.low), 0.05) > np.absolute(df.open - df.close))
            & (df.open > (df.high - np.multiply((df.high - df.low), 0.05))),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def long_legged_doji(self):
        pattern = CandlestickPatternEnum.LONG_LEGGED_DOJI
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df_tmp = self._dataframe_patterns.get(CandlestickPatternEnum.DOJI).copy()
        df_tmp["l10"] = np.multiply((df_tmp.high - df_tmp.low), 0.1)
        df_tmp["l30"] = np.multiply((df_tmp.high - df_tmp.low), 0.3)
        df_tmp["isupper"] = np.where(
            (((df_tmp.high - df_tmp.l10) > df_tmp.open) & ((df_tmp.high - df_tmp.l30) < df_tmp.open)), True, False
        )
        df_tmp["islower"] = np.where(
            (((df_tmp.low + df_tmp.l10) < df_tmp.open) & ((df_tmp.low + df_tmp.l30) > df_tmp.open)), True, False
        )

        df = self._dataframe.copy()
        df[pattern.value] = np.where(df_tmp.doji & (df_tmp.isupper | df_tmp.islower), True, False)
        self._dataframe_patterns[pattern] = df
        del df_tmp
        return df

    def hammer_hanging_man(self):
        pattern = CandlestickPatternEnum.HAMMER
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            (
                (((df.close - df.low) > (df.high - df.open) * 2) & (df.close >= df.open))
                | (((df.open - df.low) > (df.high - df.close) * 2) & (df.open >= df.close))
            ),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def inv_hammer(self):
        pattern = CandlestickPatternEnum.INV_HAMMER
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            (
                (((df.high - df.close) > (df.close - df.low) * 2) & (df.close > df.open))
                | (((df.high - df.open) > (df.open - df.low) * 2) & (df.open > df.close))
            ),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def spinning_top(self):
        pattern = CandlestickPatternEnum.SPINNING_TOP
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            (
                (df.close >= (df.low + ((df.high - df.low) / 3)))
                & (df.open >= (df.low + ((df.high - df.low) / 3)))
                & (df.close <= (df.high - ((df.high - df.low) / 3)))
                & (df.open <= (df.high - ((df.high - df.low) / 3)))
            ),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def marubozu(self):
        pattern = CandlestickPatternEnum.MARUBOZU
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[f"{MarketEnum.BULL.value}_{pattern.value}"] = np.where(
            ((df.open < df.close) & (df.open == df.low) & (df.close == df.high)), True, False
        )
        df[f"{MarketEnum.BEAR.value}_{pattern.value}"] = np.where(
            ((df.open > df.close) & (df.open == df.high) & (df.close == df.low)), True, False
        )
        self._dataframe_patterns[pattern] = df
        return df

    def engulf(self):
        pattern = CandlestickPatternEnum.ENGULF
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[f"{MarketEnum.BULL.value}_{pattern.value}"] = np.where(
            (
                (df.close.shift(1) > df.open.shift(1))
                & (df.high.shift(1) < df.high)
                & (df.low.shift(1) > df.low)
                & (df.open.shift(1) < df.close)
                & (df.close.shift(1) > df.open)
            ),
            True,
            False,
        )
        df[f"{MarketEnum.BEAR.value}_{pattern.value}"] = np.where(
            (
                (df.open.shift(1) > df.close.shift(1))
                & (df.high.shift(1) < df.high)
                & (df.low.shift(1) > df.low)
                & (df.close.shift(1) < df.open)
                & (df.open.shift(1))
                > df.close
            ),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def engulfing(self):
        pattern = CandlestickPatternEnum.ENGULFING
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df["SBullEngulf"] = np.where(
            ((df.close.shift(1) > df.open.shift(1)) & (df.high.shift(1) < df.close) & (df.low.shift(1) > df.open)),
            True,
            False,
        )
        df["SBearEngulf"] = np.where(
            ((df.open.shift(1) > df.close.shift(1)) & (df.high.shift(1) < df.open) & (df.low.shift(1) > df.open)),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def harami(self):
        pattern = CandlestickPatternEnum.HARAMI
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[f"{MarketEnum.BULL.value}_{pattern.value}"] = np.where(
            ((df.high <= df.open.shift(1)) & (df.low >= df.close.shift(1)) & (df.close > df.open)), True, False
        )
        df[f"{MarketEnum.BEAR.value}_{pattern.value}"] = np.where(
            ((df.high <= df.close.shift(1)) & (df.low >= df.open.shift(1)) & (df.close < df.open)), True, False
        )
        self._dataframe_patterns[pattern] = df
        return df

    def dark_cloud_cover(self):
        pattern = CandlestickPatternEnum.DARK_CLOUD_COVER
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            (
                (df.close.shift(1) > df.open.shift(1))
                & (((df.close.shift(1) + df.open.shift(1)) / 2) > df.close)
                & (df.open > df.close)
                & (df.open > df.close.shift(1))
                & (df.close > df.open.shift(1))
                & ((df.open - df.close) / (0.001 + (df.high - df.low)) > 0.6)
            ),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df

    def piercing(self):
        pattern = CandlestickPatternEnum.PIERCING
        # Get from cache if already calculated
        if self._dataframe_patterns.get(pattern) is not None:
            return self._dataframe_patterns[pattern]

        # Calculate and store in cache
        df = self._dataframe.copy()
        df[pattern.value] = np.where(
            (
                (df.close.shift(1) < df.open.shift(1))
                & (((df.close.shift(1) + df.open.shift(1)) / 2) < df.close)
                & (df.open < df.close)
                & (df.open < df.close.shift(1))
                & (df.close < df.open.shift(1))
                & ((df.open - df.close) / (0.001 + (df.high - df.low)) < 0.6)
            ),
            True,
            False,
        )
        self._dataframe_patterns[pattern] = df
        return df
