package com.neko233.datetime.timezone;

/**
 * @author SolarisNeko on 2023-04-23
 **/
public class TimeZoneHelper {

    /**
     * 获取时间偏移的文本
     *
     * @param timeZoneHourId 时区id
     * @return "+8" / "-8"
     */
    public static String getOffsetStringByHourId(int timeZoneHourId) {
        if (timeZoneHourId >= 0) {
            return "+" + timeZoneHourId;
        }
        return "-" + timeZoneHourId;
    }

}
