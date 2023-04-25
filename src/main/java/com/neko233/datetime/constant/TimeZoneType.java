package com.neko233.datetime.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author SolarisNeko on 2023-04-23
 **/
public enum TimeZoneType {

    /**
     * UTC：Coordinated Universal Time 协调世界时。<br>
     * 因为地球自转越来越慢，每年都会比前一年多出零点几秒，每隔几年协调世界时组织都会给世界时+1秒，<br>
     * 让基于原子钟的世界时和基于天文学（人类感知）的格林尼治标准时间相差不至于太大。并将得到的时间称为 UTC，这是现在使用的【世界标准时间】。
     */
    UTC,

    /**
     * [Default] <br>
     * GreenwichMeanTime，GMT. <br>
     * 格林尼治所在地的标准时间, 格林尼治是英国伦敦南郊原格林威治天文台的所在地，它又是世界上地理经度的起始点。
     */
    GMT,

    /**
     * Central Standard Time, CST <br>
     * 非常不建议使用.
     * 同时代表着 4 个不同的时区。<br>
     * <code>
     * Central Standard Time (USA) UT-6:00
     * Central Standard Time (Australia) UT+9:30
     * China Standard Time UT+8:00
     * Cuba Standard Time UT-4:00
     * </code>
     */
    CST,

    ;

    private static final Map<TimeZoneType, String> _cachedMap = new HashMap<>();

    static {
        for (TimeZoneType value : TimeZoneType.values()) {
            _cachedMap.put(value, value.name().toUpperCase());
        }
    }

    public static String getNameByType(TimeZoneType timeZoneType) {
        return _cachedMap.get(timeZoneType);
    }
}
