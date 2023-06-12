package com.neko233.datetime;

import com.neko233.datetime.api.DateTimeApi;
import com.neko233.datetime.constant.DateTimeToken;
import com.neko233.datetime.constant.Month233;
import com.neko233.datetime.timezone.TimeZone233;
import com.neko233.datetime.utils.AssertUtilsForDt;
import com.neko233.datetime.utils.KvTemplateForDt;
import com.neko233.datetime.utils.TextTokenUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class DateTime233 implements DateTimeApi<DateTime233> {

    // 可以自定义修改成【统一的默认时区】
    private static final AtomicReference<TimeZone> DEFAULT_TIME_ZONE = new AtomicReference<>(TimeZone.getDefault());


    public static final int MAX_MONTH_COUNT = 12;

    public boolean isLearYear() {
        return DateTime233.isLeapYear(this.year);
    }

    public long toMsFrom1970() {
        return this.zonedTimeMs;
    }

    public static class Constant {
        // 1000 ms in 1s
        public static final int MS_IN_1_SECOND = 1000;
        // 60 second in 1 min
        public static final int SECOND_IN_1_MINUTE = 60;
        // 60 minute in 1 hour
        public static final int MINUTE_IN_1_HOUR = 60;
        // 24 hour in 1 day
        public static final int HOUR_IN_1_DAY = 24;
        // start year = 1970-01-01 00:00:00
        public static final int START_YEAR_NUMBER = 1970;
        // 1 day millisSecond
        public static final long MS_IN_1_DAY = 24L * 60 * 60 * 1000;
    }

    // 0 时区下的毫秒数 from 1970-01-01 00:00:00
    private final long originalTimeMs;
    // 当前时区下的毫秒数 from 1970-01-01 00:00:00
    private final long zonedTimeMs;

    // 时区id. example = +8, +0
    private final int timeZoneId;
    // 时区导致的 timeMs 偏移毫秒数
    private final int offsetByTimeZoneMs;
    // 时区 by JDK
    private final TimeZone timeZone;


    // yyyy-MM-dd HH:mm:ss,SSS
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;
    private final int millisSecond;

    /**
     * 修改默认时区
     *
     * @param timeZone 时区
     */
    public static void setDefaultTimeZone(@Nullable TimeZone timeZone) {
        if (timeZone == null) {
            throw new IllegalArgumentException("默认时区不允许为空! 修改失败! fail.");
        }
        DEFAULT_TIME_ZONE.compareAndSet(DEFAULT_TIME_ZONE.get(), timeZone);
    }

    /**
     * 零点时刻. 当天的 yyyy-MM-dd 00:00:00 分
     *
     * @param originalMs 时间戳
     * @return DateTime233
     */
    public static DateTime233 ofZeroClock(long originalMs) {
        return new DateTime233(originalMs).toZeroClock();
    }

    public static DateTime233 of(long originalMs) {
        return new DateTime233(originalMs);
    }

    /**
     * @param jdkDate JDK-8 之前的 {@link java.util.Date}
     * @return DateTime233
     */
    public static DateTime233 from(@Nullable Date jdkDate) {
        AssertUtilsForDt.isNotNull(jdkDate, "your Date can not be null! NPE");
        long originalTimeMs = jdkDate.getTime();
        return DateTime233.of(originalTimeMs);
    }


    /**
     * @param jdkDateTime JDK-8+ 的 {@link java.time.LocalDateTime}
     * @return DateTime233
     */
    public static DateTime233 from(@Nullable LocalDateTime jdkDateTime) {
        AssertUtilsForDt.isNotNull(jdkDateTime, "your jdkDateTime can not be null! NPE");

        final ZoneOffset offset = OffsetDateTime.now()
                .getOffset();
        long originalTimeMs = jdkDateTime.toInstant(offset)
                .getEpochSecond() * 1000;
        return DateTime233.of(originalTimeMs);
    }

    /**
     * @param jdkZonedDateTime JDK 8+ 版本, 的时区日期时间 {@link java.time.ZonedDateTime}
     * @return DateTime233
     */
    public static DateTime233 from(ZonedDateTime jdkZonedDateTime) {
        long originalTimeMs = jdkZonedDateTime.toInstant()
                .getEpochSecond() * 1000;
        return DateTime233.of(originalTimeMs);
    }

    private DateTime233(long originalTimeMs) {
        this(originalTimeMs, DEFAULT_TIME_ZONE.get());
    }

    public static DateTime233 of(String dateTimeText,
                                 String format) {
        Map<String, String> map = TextTokenUtils.matchFullyToTokenMap(dateTimeText,
                format,
                DateTimeToken.DATE_TIME_TOKEN_LIST);

        int year = Integer.parseInt(String.valueOf(map.getOrDefault("yyyy",
                "0")));
        int month = Integer.parseInt(String.valueOf(map.getOrDefault("MM",
                "0")));
        int day = Integer.parseInt(String.valueOf(map.getOrDefault("dd",
                "0")));
        int hour = Integer.parseInt(String.valueOf(map.getOrDefault("HH",
                "0")));
        int minute = Integer.parseInt(String.valueOf(map.getOrDefault("mm",
                "0")));
        int second = Integer.parseInt(String.valueOf(map.getOrDefault("ss",
                "0")));
        int millisSecond = Integer.parseInt(String.valueOf(map.getOrDefault("SSS",
                "0")));

        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                DEFAULT_TIME_ZONE.get()
                        .getRawOffset()
        );
    }

    public DateTime233(long originalTimeMs,
                       TimeZone inputTimeZone) {

        this.timeZone = Optional.ofNullable(inputTimeZone)
                .orElse(DEFAULT_TIME_ZONE.get());

        this.originalTimeMs = originalTimeMs;

        // 时区加成 GMT offset
        int gmtOffsetMs = DEFAULT_TIME_ZONE.get()
                .getRawOffset();
        this.offsetByTimeZoneMs = gmtOffsetMs;
        this.timeZoneId = Math.toIntExact(TimeUnit.MILLISECONDS.toHours(gmtOffsetMs));

        long gmtZoneMs = originalTimeMs + gmtOffsetMs;

        this.zonedTimeMs = gmtZoneMs;


        this.millisSecond = (int) (gmtZoneMs % 1000);

        long seconds = gmtZoneMs / Constant.MS_IN_1_SECOND;
        long minutes = seconds / Constant.SECOND_IN_1_MINUTE;
        long hours = minutes / Constant.MINUTE_IN_1_HOUR;
        long days = hours / Constant.HOUR_IN_1_DAY;

        // 计算年份
        int year = Constant.START_YEAR_NUMBER;
        while (true) {
            long daysOfYear = (year % 4 == 0 && year % 100 != 0) || year % 400 == 0 ? 366 : 365;
            if (days >= daysOfYear) {
                days -= daysOfYear;
                year++;
            } else {
                break;
            }
        }
        this.year = year;

        // 计算月份和日期
        int m = 1;
        int[] daysOfMonth = {31, this.year % 4 == 0 && this.year % 100 != 0 || this.year % 400 == 0 ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int i = 0; i < daysOfMonth.length; i++) {
            if (days >= daysOfMonth[i]) {
                days -= daysOfMonth[i];
                m++;
            } else {
                break;
            }
        }
        this.month = m;
        this.day = (int) (days + 1);

        // 计算时分秒
        this.hour = (int) (hours % 24);
        this.minute = (int) (minutes % 60);
        this.second = (int) (seconds % 60);
    }


    public DateTime233(int year,
                       int month,
                       int day,
                       int hour,
                       int minute,
                       int second,
                       int millisSecond,
                       int offsetByTimeZoneMs) {
        this(calculateOffsetMs(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                offsetByTimeZoneMs
        ));
    }

    public static DateTime233 now() {
        return new DateTime233(TimeSource233.currentMs());
    }

    @Override
    public int getOffsetMs() {
        return this.offsetByTimeZoneMs;
    }

    @Override
    public int getZoneId() {
        return this.timeZoneId;
    }

    @Override
    public DateTime233 toZeroClock() {
        return new DateTime233(year,
                month,
                day,
                0,
                0,
                0,
                0,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int year() {
        return this.year;
    }

    @Override
    public DateTime233 year(int year) {
        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int month() {
        return this.month;
    }

    @Override
    public DateTime233 month(int month) {
        DateTimeChecker233.isOkMonth(month);

        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int day() {
        return this.day;
    }

    @Override
    public DateTime233 day(int day) {
        DateTimeChecker233.isDayOk(this.year,
                this.month,
                this.day);

        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int hour() {
        return this.hour;
    }

    @Override
    public DateTime233 hour(int hour) {
        DateTimeChecker233.isHourOk(hour);

        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int minute() {
        return this.minute;
    }

    @Override
    public DateTime233 minute(int minute) {
        DateTimeChecker233.isMinuteOk(minute);

        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int second() {
        return this.second;
    }

    @Override
    public DateTime233 second(int second) {
        DateTimeChecker233.isSecondOk(second);

        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int millisSecond() {
        return this.millisSecond;
    }


    @Override
    public DateTime233 millisSecond(int millisSecond) {
        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                this.offsetByTimeZoneMs);
    }

    @Override
    public int timeZoneOffsetMs() {
        return this.offsetByTimeZoneMs;
    }

    @Override
    public DateTime233 getTimeZone(int timeZoneHourId) {
        int timeZoneOffsetMs = TimeZone233.getZoneOffsetMs(timeZoneHourId);
        return new DateTime233(year,
                month,
                day,
                hour,
                minute,
                second,
                millisSecond,
                timeZoneOffsetMs);
    }

    /**
     * 判断是否为闰年 (4/100/400)
     *
     * @return is leap ?
     */
    public static boolean isLeapYear(int year) {
        boolean isFour = year % 4 == 0;
        boolean isHundred = year % 100 != 0;
        boolean isFourHundred = year % 400 == 0;
        return (isFour && isHundred) || isFourHundred;
    }

    // 获取某年某月的天数
    public static int getDaysInMonth(int year,
                                     int month) {
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            case 2:
                return isLeapYear(year) ? 29 : 28;
            default:
                throw new IllegalArgumentException(String.format("your year = %d, month = %d is error!",
                        year,
                        month));
        }
    }

    @Override
    public DateTime233 plusYears(int year) {
        int newYear = this.year + year;
        long nextMs = calculateOffsetMs(newYear,
                this.month,
                this.day,
                this.hour,
                this.minute,
                this.second,
                this.millisSecond,
                this.offsetByTimeZoneMs);

        return new DateTime233(nextMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusYears(int year) {
        return this.plusYears(-year);
    }

    @Override
    public DateTime233 plusMonths(int plusMonth) {
        int finalMonth = this.month + plusMonth;

        int resultMonth = (finalMonth - 1) % 12 + 1;

        // 正数
        int calcYear = (finalMonth - 1) / 12;
        int resultYear = this.year + calcYear;

        // 负数情况
        if (resultMonth < 0) {
            resultYear = resultYear - calcYear - 1;
            resultMonth = 12 + resultMonth;
        }

        long nextMs = calculateOffsetMs(resultYear,
                resultMonth,
                this.day,
                this.hour,
                this.minute,
                this.second,
                this.millisSecond,
                this.offsetByTimeZoneMs);
        return new DateTime233(nextMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusMonths(int month) {
        return this.plusMonths(-month);
    }


    @Override
    public DateTime233 plusDays(int days) {
        long newOriginMs = this.originalTimeMs + TimeUnit.DAYS.toMillis(days);
        return new DateTime233(newOriginMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusDays(int days) {
        return this.plusDays(-days);
    }

    @Override
    public DateTime233 plusHours(int hour) {
        long newOriginMs = this.originalTimeMs + TimeUnit.HOURS.toMillis(hour);
        return new DateTime233(newOriginMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusHours(int hour) {
        return this.plusHours(-hour);
    }

    @Override
    public DateTime233 plusMinutes(int minute) {
        long newOriginMs = this.originalTimeMs + TimeUnit.MINUTES.toMillis(minute);
        return new DateTime233(newOriginMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusMinutes(int minute) {
        return this.plusMinutes(-minute);
    }

    @Override
    public DateTime233 plusSeconds(int second) {
        long newOriginMs = this.originalTimeMs + TimeUnit.SECONDS.toMillis(second);
        return new DateTime233(newOriginMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusSeconds(int second) {
        return this.plusSeconds(-second);
    }

    @Override
    public DateTime233 plusMillisSecond(int millisSecond) {
        long newOriginMs = this.originalTimeMs + millisSecond;
        return new DateTime233(newOriginMs,
                this.timeZone);
    }

    @Override
    public DateTime233 minusMillisSecond(int millisSecond) {
        return this.plusMillisSecond(-millisSecond);
    }

    @Override
    public long getOriginalTimeMs() {
        return this.originalTimeMs;
    }

    @Override
    public long getZoneTimeMs() {
        return this.zonedTimeMs;
    }


    /**
     * @return 时区 by JDK
     */
    public TimeZone getTimeZone() {
        return (TimeZone) this.timeZone.clone();
    }

    /**
     * to jdk DateTime
     *
     * @return 转换为 JDK 的 DateTime
     */
    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.of(year,
                month,
                day,
                hour,
                minute,
                second);
    }

    /**
     * 计算从 1970-01-01 00:00:00 到现在过了多少时间
     *
     * @param year   年
     * @param month  月
     * @param day    日
     * @param hour   时
     * @param minute 分
     * @param second 秒
     * @return 已过的时间戳
     */
    public static long calculateOffsetMs(int year,
                                         int month,
                                         int day,
                                         int hour,
                                         int minute,
                                         int second,
                                         int millisSecond,
                                         int zoneOffsetMs
    ) {
        int offsetYear = Math.max(0,
                year - Constant.START_YEAR_NUMBER);
        int toCalcLeapYear = offsetYear + 1;
        // year's day count = 4/400 闰年, 100 非闰年
        long totalElapsedDays = offsetYear * 365L + toCalcLeapYear / 4 - toCalcLeapYear / 100 + toCalcLeapYear / 400;

        // month's day count
        if (month > 2 && isLeapYear(year)) {
            totalElapsedDays++;
        }
        // 此处不需要考虑闰年 +1 day, year done
        int minMonth = Math.min(month - 1,
                MAX_MONTH_COUNT);
        for (int i = 0; i < minMonth; i++) {
            totalElapsedDays += Month233.DAYS_IN_MONTH_ARRAY[i];
        }
        // just day count
        totalElapsedDays += day - 1;

        // 3124137600000,
        long totalMs = TimeUnit.DAYS.toMillis(totalElapsedDays)
                + TimeUnit.HOURS.toMillis(hour)
                + TimeUnit.MINUTES.toMillis(minute)
                + second * 1000L
                + millisSecond;

        return totalMs - zoneOffsetMs;
    }

    @Override
    public int getWeekDay() {
        return DateTime233.weekDay(this.zonedTimeMs);
    }

    @Override
    public int getWhichYear() {
        return DateTime233.whichYear(this.zonedTimeMs);
    }

    private static int whichYear(long zoneMs) {
        long timeMs = zoneMs;
        // 计算年份和月份
        int year = 1970;
        while (true) {
            int daysInYear = isLeapYear(year) ? 366 : 365;
            if (timeMs < daysInYear * 86400000L) {
                break;
            }
            timeMs -= daysInYear * 86400000L;
            year++;
        }
        return year;
    }


    /**
     * @return 今年里的第多少天
     */
    @Override
    public int getDayOfYear() {
        return dayOfYear(this.zonedTimeMs);
    }

    public static int dayOfYear(long zoneMs) {
        long timeMs = zoneMs;
        // 计算年份和月份
        int year = 1970;
        while (true) {
            int daysInYear = isLeapYear(year) ? 366 : 365;
            if (timeMs < daysInYear * 86400000L) {
                break;
            }
            timeMs -= daysInYear * 86400000L;
            year++;
        }

        int month = 1;
        int numDays = 0;
        while (true) {
            int daysInMonth = getDaysInMonth(year,
                    month);
            if (timeMs < daysInMonth * 86400000L) {
                break;
            }
            timeMs -= daysInMonth * 86400000L;
            numDays += daysInMonth;
            month++;
        }
        // 计算日期
        int dayOfMonth = (int) (timeMs / 86400000L) + 1;
        return numDays + dayOfMonth;
    }


    /**
     * Zeller's congruence（蔡勒公式）来计算今天是周几
     *
     * @param timeMs 毫秒
     * @return 星期几
     */
    public static int weekDay(long timeMs) {
        // 计算年份和月份
        int year = whichYear(timeMs);
        int dayOfYears = dayOfYear(timeMs);

        int tempDayOfMonth = dayOfYears;
        int month = 1;
        while (true) {
            int fixedDayInMonth = getDaysInMonth(year,
                    month);
            if (tempDayOfMonth < fixedDayInMonth) {
                break;
            }
            tempDayOfMonth -= fixedDayInMonth;
            if (tempDayOfMonth == 0) {
                tempDayOfMonth = fixedDayInMonth;
                break;
            }
            month++;
        }
        if (month > 12) {
            year += month / 12;
        }
        // 计算日期
        int dayOfMonth = tempDayOfMonth;
        // 计算世纪数和年份后两位
        int century = year / 100;
        int yearOfCentury = year % 100;
        // 计算蔡勒公式中的参数
        int m = month;
        int c = century % 4;
        if (month == 1 || month == 2) {
            m = month + 12;
            yearOfCentury--;
            if (yearOfCentury < 0) {
                yearOfCentury += 100;
                century--;
            }
        }
        // 计算蔡勒公式中的 w
        int w = yearOfCentury + yearOfCentury / 4 + c / 4 - 2 * c + 26 * (m + 1) / 10 + dayOfMonth - 1;
        // 将 w 转换为星期几的数字
        int weekday = w % 7;
        if (weekday < 0) {
            weekday += 7;
        }
        // 周日算第 7 天
        return weekday == 0 ? 7 : weekday;
    }


    @Override
    public String toString() {
        return format("yyyy-MM-dd HH:mm:ss");
    }

    public String toString(String dateTimeFormat) {
        return format(dateTimeFormat);
    }

    @NotNull
    public String format(String formatStyle) {
        Map<String, Object> kv = paramContextMap();
        String newFormat = generateTemplate(formatStyle,
                kv);
        return KvTemplateForDt.builder(newFormat)
                .put(kv)
                .build();
    }

    @Override
    public Date toDate() {
        return new Date(this.originalTimeMs);
    }

    private static String generateTemplate(String formatStyle,
                                           Map<String, ? extends Object> kv) {
        String newFormat = formatStyle;
        for (String s : kv.keySet()) {
            newFormat = newFormat.replaceAll(s,
                    "\\${" + s + "}");
        }
        return newFormat;
    }


    private Map<String, Object> paramContextMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("yyyy",
                String.format("%04d",
                        this.year));
        map.put("MM",
                String.format("%02d",
                        this.month));
        map.put("dd",
                String.format("%02d",
                        this.day));
        map.put("HH",
                String.format("%02d",
                        this.hour));
        map.put("mm",
                String.format("%02d",
                        this.minute));
        map.put("ss",
                String.format("%02d",
                        this.second));
        map.put("SSS",
                String.format("%03d",
                        this.millisSecond));
        return map;
    }

}



