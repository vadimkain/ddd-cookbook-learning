package com.example.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class DateTimeUtils {

    public static LocalDateTime nowToZonedLocalDataTime() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static long nowToEpochMill() {
        return nowToZonedLocalDataTime().atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
