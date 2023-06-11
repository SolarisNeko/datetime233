package com.neko233.datetime.unit;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko
 * Date on 2023-05-30
 */
@Getter
public enum DateTimeUnit {

    YEAR("year"), // 年


    MONTH("month"), // 月


    DAY(TimeUnit.DAYS, "day"),  // 日


    HOUR(TimeUnit.HOURS, "hour"),     // 小时


    MINUTE(TimeUnit.MINUTES, "minute"),    // 分钟


    SECOND(TimeUnit.SECONDS, "second"),   // 秒


    MS(TimeUnit.MILLISECONDS, "ms"),     // 毫秒

    ;

    final TimeUnit converTimeUnit;
    final String dateTimeText;

    DateTimeUnit(String dateTimeText) {
        this.converTimeUnit = null;
        this.dateTimeText = dateTimeText;
    }

    DateTimeUnit(TimeUnit converTimeUnit, String dateTimeText) {
        this.converTimeUnit = converTimeUnit;
        this.dateTimeText = dateTimeText;
    }
}
