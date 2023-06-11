package com.neko233.datetime.api;


import com.neko233.datetime.unit.DateTimeUnit;
import com.neko233.datetime.constant.Month233;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko on 2023-04-23
 **/
public interface DateTimeApi<T extends DateTimeApi<?>> {

    /**
     * {@link java.util.Date} 使用该毫秒, 因其自带时区有 bug
     *
     * @return 无时区偏移的 time ms | 如 UTC+0 时区 ms
     */
    default long originalTimeMs() {
        return getOriginalTimeMs();
    }


    long getOriginalTimeMs();

    /**
     * @return 带时区偏移后的 time ms
     */
    default long zoneTimeMs() {
       return getZoneTimeMs();
    }


    long getZoneTimeMs();

    /**
     * @return 当前时区
     */
    TimeZone getTimeZone();

    /**
     * @return 转为 JDK DateTime
     */
    LocalDateTime toLocalDateTime();

    /**
     * @return 星期几 ?
     */
    int getWeekDay();

    /**
     * @return 哪一年
     */
    int getWhichYear();

    /**
     * @return 一年中的第几天
     */
    int getDayOfYear();

    /**
     * 闰年 = 4, 100, 400 年/次 + 1 day
     *
     * @return 是否是闰年
     */
    boolean isLearYear();

    /**
     * @return 从1970到现在的毫秒数
     */
    long toMsFrom1970();

    /**
     * @return default utc 时区偏移毫秒数
     */
    int getOffsetMs();

    /**
     * @return 时区id
     */
    int getZoneId();

    /**
     * @return 年
     */
    int year();

    /**
     * setter
     *
     * @param year 年
     * @return DateTime
     */
    T year(int year);

    /**
     * @return 月
     */
    int month();

    /**
     * @param month 月
     * @return newOne
     */
    T month(int month);

    /**
     * @return 日
     */
    int day();

    /**
     * @param day 日
     * @return newOne
     */
    T day(int day);

    /**
     * @return 时
     */
    int hour();

    /**
     * @param hour 时
     * @return newOne
     */
    T hour(int hour);

    /**
     * @return 分
     */
    int minute();

    /**
     * @param minute 分
     * @return newOne
     */
    T minute(int minute);

    /**
     * @return 秒
     */
    int second();

    /**
     * @param second 秒
     * @return 秒
     */
    T second(int second);

    /**
     * @return 毫秒
     */
    int millisSecond();

    /**
     * @param millisSecond 毫秒
     * @return 毫秒
     */
    T millisSecond(int millisSecond);

    int timeZoneOffsetMs();

    /**
     * @param timeZoneOffsetHourId 时区偏移, 小时id. 例如 +8 时区 = 8, -8 时区 = -8
     * @return new
     */
    T getTimeZone(int timeZoneOffsetHourId);

    T plusYears(int year);

    T minusYears(int year);

    T plusMonths(int plusMonth);

    T minusMonths(int month);

    T plusDays(int days);

    T minusDays(int days);

    T plusHours(int hour);

    T minusHours(int hour);

    T plusMinutes(int minute);

    T minusMinutes(int minute);

    T plusSeconds(int second);

    T minusSeconds(int second);

    T plusMillisSecond(int millisSecond);

    T minusMillisSecond(int millisSecond);


    String toString();

    String format(String formatStyle);

    Date toDate();


    /**
     * 是否相等
     *
     * @param other datetime233
     * @return is same to others ?
     */
    default boolean isEquals(T other) {
        if (other == null) {
            return true;
        }
        return Objects.equals(originalTimeMs(), other.originalTimeMs());
    }

    /**
     * 是否在另一个时间之前 ?
     *
     * @param other datetime233
     * @return is before others ?
     */
    default boolean isBefore(T other) {
        if (other == null) {
            return true;
        }
        return originalTimeMs() < other.originalTimeMs();
    }

    /**
     * @return is after others ?
     */
    default boolean isAfter(T other) {
        if (other == null) {
            return true;
        }
        return originalTimeMs() > other.originalTimeMs();
    }

    /**
     * 与另外一个时间相比的差值
     *
     * @param other    dateTime233
     * @param timeUnit 提供时间格式
     * @return 该格式的时间差
     */
    default long diffByTimeUnit(T other,
                                TimeUnit timeUnit) {
        if (other == null || timeUnit == null) {
            return 0L;
        }
        long diffMs = originalTimeMs() - other.originalTimeMs();
        return timeUnit.convert(diffMs, TimeUnit.MILLISECONDS);
    }


    /**
     * 只返回绝对值
     *
     * @param other        其他
     * @param dateTimeUnit 单位
     * @return 数值
     */
    default long diff(T other,
                      DateTimeUnit dateTimeUnit) {
        if (other == null || dateTimeUnit == null) {
            return 0L;
        }
        TimeUnit jdkTimeUnit = dateTimeUnit.getConverTimeUnit();
        if (jdkTimeUnit != null) {
            return diffAbsByTimeUnit(other, jdkTimeUnit);
        }

        // 年
        int yearDiffCount = Math.abs(other.year() - this.year());
        if (dateTimeUnit == DateTimeUnit.YEAR) {
            return yearDiffCount;
        }
        // 月
        if (dateTimeUnit == DateTimeUnit.MONTH) {
            int thisMonth = this.month();
            int otherMonth = other.month();

            int diffYear = Math.min(this.year(), other.year());

            int thisCalcYear = this.year() - diffYear;
            int otherCalcYear = other.year() - diffYear;
            int thisMonthValue = thisCalcYear * Month233.COUNT_FOR_MONTH + thisMonth;
            int otherMonthValue = otherCalcYear * Month233.COUNT_FOR_MONTH + otherMonth;

            return Math.abs(otherMonthValue - thisMonthValue);
        }
        return 0L;
    }

    /**
     * @param other    dateTime233
     * @param timeUnit 提供时间格式
     * @return 该格式的时间差, 绝对值
     */
    default long diffAbsByTimeUnit(T other,
                                   TimeUnit timeUnit) {
        return Math.abs(diffByTimeUnit(other, timeUnit));
    }


    /**
     * 零点时刻. 当天的 yyyy-MM-dd 00:00:00 分
     *
     * @return T
     */
    T toZeroClock();
}
