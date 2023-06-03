package com.neko233.datetime.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko
 * Date on 2023-05-30
 */
@Getter
public enum DateTimeUnit {

    YEAR, // 年


    MONTH, // 月


    DAY(TimeUnit.DAYS),  // 日


    HOUR(TimeUnit.HOURS),     // 小时


    MINUTE(TimeUnit.MINUTES),    // 分钟


    SECOND(TimeUnit.SECONDS),   // 秒


    MILLISECOND(TimeUnit.MILLISECONDS),     // 毫秒

    ;

    final TimeUnit converTimeUnit;

    DateTimeUnit() {
        this.converTimeUnit = null;
    }

    DateTimeUnit(TimeUnit converTimeUnit) {
        this.converTimeUnit = converTimeUnit;
    }
}
