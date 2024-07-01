package com.tradey.technical_analysis.pkgs;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class Time {
    public static ZonedDateTime formatUnixMsToDateTime(long unixMs) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(unixMs), ZoneOffset.UTC);
    }
}
