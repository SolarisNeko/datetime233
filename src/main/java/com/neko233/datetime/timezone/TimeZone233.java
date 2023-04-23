package com.neko233.datetime.timezone;

import com.neko233.datetime.constant.TimeZoneType;
import org.apache.commons.lang3.StringUtils;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author LuoHaoJun on 2023-04-23
 **/
public class TimeZone233 {

    /**
     * 虽然有 +3.5, 但是大部分不常用, 暂时忽略
     */
    private static final Map<String, Integer> zoneIdToMsOffset = new HashMap<>() {{
        put("+0", 0);
        put("+1", Math.toIntExact(TimeUnit.HOURS.toMillis(+1)));
        put("+2", Math.toIntExact(TimeUnit.HOURS.toMillis(+2)));
        put("+3", Math.toIntExact(TimeUnit.HOURS.toMillis(+3)));
        put("+4", Math.toIntExact(TimeUnit.HOURS.toMillis(+4)));
        put("+5", Math.toIntExact(TimeUnit.HOURS.toMillis(+5)));
        put("+6", Math.toIntExact(TimeUnit.HOURS.toMillis(+6)));
        put("+7", Math.toIntExact(TimeUnit.HOURS.toMillis(+7)));
        put("+8", Math.toIntExact(TimeUnit.HOURS.toMillis(+8)));
        put("+9", Math.toIntExact(TimeUnit.HOURS.toMillis(+9)));
        put("+10", Math.toIntExact(TimeUnit.HOURS.toMillis(+10)));
        put("+11", Math.toIntExact(TimeUnit.HOURS.toMillis(+11)));
        put("+12", Math.toIntExact(TimeUnit.HOURS.toMillis(+12)));
        put("-1", Math.toIntExact(TimeUnit.HOURS.toMillis(-1)));
        put("-2", Math.toIntExact(TimeUnit.HOURS.toMillis(-2)));
        put("-3", Math.toIntExact(TimeUnit.HOURS.toMillis(-3)));
        put("-4", Math.toIntExact(TimeUnit.HOURS.toMillis(-4)));
        put("-5", Math.toIntExact(TimeUnit.HOURS.toMillis(-5)));
        put("-6", Math.toIntExact(TimeUnit.HOURS.toMillis(-6)));
        put("-7", Math.toIntExact(TimeUnit.HOURS.toMillis(-7)));
        put("-8", Math.toIntExact(TimeUnit.HOURS.toMillis(-8)));
        put("-9", Math.toIntExact(TimeUnit.HOURS.toMillis(-9)));
        put("-10", Math.toIntExact(TimeUnit.HOURS.toMillis(-10)));
        put("-11", Math.toIntExact(TimeUnit.HOURS.toMillis(-11)));
        put("-12", Math.toIntExact(TimeUnit.HOURS.toMillis(-12)));
    }};

    /**
     * ZoneId 表示 UTC 的时区
     *
     * @param timeZoneHourId +8 时区 = 8, -8 时区 = -8
     * @return 时区
     */
    public static ZoneId getZoneId(int timeZoneHourId) {
        return getZoneId(TimeZoneType.UTC, timeZoneHourId);
    }

    private static ZoneId getZoneId(String zoneIdStr) {
        return ZoneId.of(Optional.ofNullable(zoneIdStr).orElse("gmt+0"));
    }

    private static ZoneId getZoneId(TimeZoneType timeZoneType,
                                    String timeZoneHourId) {
        String zone = TimeZoneType.getNameByType(timeZoneType);
        if (StringUtils.isBlank(zone)) {
            return null;
        }
        return ZoneId.of(zone + timeZoneHourId);
    }

    private static ZoneId getZoneId(TimeZoneType timeZoneType,
                                    int timeZoneHourId) {

        String timeZoneStr = TimeZoneHelper.getOffsetStringByHourId(timeZoneHourId);

        String zone = TimeZoneType.getNameByType(timeZoneType);
        if (StringUtils.isBlank(zone)) {
            return null;
        }

        String fullZoneName = zone + timeZoneStr;
        return ZoneId.of(fullZoneName);
    }

    public static int getZoneOffsetMs(int timeZoneHourId) {
        String offsetStringByHourId = TimeZoneHelper.getOffsetStringByHourId(timeZoneHourId);
        return zoneIdToMsOffset.get(offsetStringByHourId);
    }
}
