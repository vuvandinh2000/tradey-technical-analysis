package com.tradey.technical_analysis.pkgs;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSxxx");

    public static ZonedDateTime formatUnixMsToDateTime(long unixMs) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(unixMs), ZoneOffset.UTC);
    }

    public static ZonedDateTime getCurrentTimestamp() {
        return ZonedDateTime.now(ZoneOffset.UTC);
    }
}
