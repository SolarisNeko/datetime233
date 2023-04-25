package com.neko233.datetime.constant;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SolarisNeko
 * Date on 2023-04-26
 */
public enum WeekDay233 {

    Monday(1),

    Tuesday(2),

    Wednesday(3),

    Thursday(4),

    Friday(5),

    Saturday(6),

    Sunday(7),
    ;

    private static final Map<Integer, WeekDay233> _cacheMap = new HashMap<>();

    static {
        for (WeekDay233 value : WeekDay233.values()) {
            _cacheMap.put(value.weekday, value);
        }
    }


    private final int weekday;

    WeekDay233(int weekdayNumber) {
        this.weekday = weekdayNumber;
    }

    /**
     * 常规周一到周日
     *
     * @return 周一 = 1, 周日 = 7
     */
    public int getWeekday() {
        return weekday;
    }


    /**
     * [Special] 周日为第一天
     *
     * @return 周日 = 1, 周六 = 7
     */
    public int getWeekdayBySundayFirst() {
        return weekday + 1 % 8 + 1;
    }

    /**
     * 获取枚举
     * @param weekday [1, 7]
     * @return WeekDay233
     */
    @Nullable
    public static WeekDay233 getEnumByWeekday(int weekday) {
        return _cacheMap.get(weekday);
    }
}
