package com.neko233.datetime.api;

import com.neko233.datetime.DateTime233;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * @author LuoHaoJun on 2023-04-23
 **/
public interface DateTimeApi {

    /**
     * {@link java.util.Date} 使用该毫秒, 因其自带时区有 bug
     *
     * @return 无时区偏移的 ms | 如 UTC+0 时区 ms
     */
    long originalMs();

    /**
     * @return 时区偏移后的 ms
     */
    long gmtZoneMs();

    /**
     * @return 当前时区
     */
    TimeZone timeZone();

    /**
     * @return 转为 JDK DateTime
     */
    LocalDateTime toLocalDateTime();

    /**
     * @return 星期几 ?
     */
    int weekDay();

    int whichYear();

    /**
     * @return 一年中的第几天
     */
    int dayOfYear();

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
     * @return 时区偏移毫秒数
     */
    int gmtOffsetMs();

    /**
     * @return 时区id
     */
    int gmtZoneId();

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
    DateTime233 year(int year);

    /**
     * @return 月
     */
    int month();

    /**
     * @param month 月
     * @return newOne
     */
    DateTime233 month(int month);

    /**
     * @return 日
     */
    int day();

    /**
     * @param day 日
     * @return newOne
     */
    DateTime233 day(int day);

    /**
     * @return 时
     */
    int hour();

    /**
     * @param hour 时
     * @return newOne
     */
    DateTime233 hour(int hour);

    /**
     * @return 分
     */
    int minute();

    /**
     * @param minute 分
     * @return newOne
     */
    DateTime233 minute(int minute);

    /**
     * @return 秒
     */
    int second();

    /**
     * @param second 秒
     * @return 秒
     */
    DateTime233 second(int second);

    /**
     * @return 毫秒
     */
    int millisSecond();

    /**
     * @param millisSecond 毫秒
     * @return 毫秒
     */
    DateTime233 millisSecond(int millisSecond);

    int timeZoneOffsetMs();

    /**
     * @param timeZoneOffsetHourId 时区偏移, 小时id. 例如 +8 时区 = 8, -8 时区 = -8
     * @return new
     */
    DateTime233 timeZone(int timeZoneOffsetHourId);

    DateTime233 plusYears(int year);

    DateTime233 minusYears(int year);

    DateTime233 plusMonths(int plusMonth);

    DateTime233 minusMonths(int month);

    DateTime233 plusDays(int days);

    DateTime233 minusDays(int days);

    DateTime233 plusHours(int hour);

    DateTime233 minusHours(int hour);

    DateTime233 plusMinutes(int minute);

    DateTime233 minusMinutes(int minute);

    DateTime233 plusSeconds(int second);

    DateTime233 minusSeconds(int second);

    DateTime233 plusMillisSecond(int millisSecond);

    DateTime233 minusMillisSecond(int millisSecond);


    String toString();

    String format(String formatStyle);

    Date toDate();


    /**
     * 是否相等
     *
     * @param other datetime233
     * @return is same to others ?
     */
    default boolean isEquals(DateTime233 other) {
        if (other == null) {
            return true;
        }
        return Objects.equals(originalMs(), other.originalMs());
    }

    /**
     * 是否在另一个时间之前 ?
     *
     * @param other datetime233
     * @return is before others ?
     */
    default boolean isBefore(DateTime233 other) {
        if (other == null) {
            return true;
        }
        return originalMs() < other.originalMs();
    }

    /**
     * @return is after others ?
     */
    default boolean isAfter(DateTime233 other) {
        if (other == null) {
            return true;
        }
        return originalMs() > other.originalMs();
    }

    /**
     * 与另外一个时间相比的差值
     *
     * @param other    dateTime233
     * @param timeUnit 提供时间格式
     * @return 该格式的时间差
     */
    default long diff(DateTime233 other, TimeUnit timeUnit) {
        if (other == null || timeUnit == null) {
            return 0L;
        }
        long diffMs = originalMs() - other.originalMs();
        return timeUnit.convert(diffMs, TimeUnit.MILLISECONDS);
    }

    /**
     * @param other    dateTime233
     * @param timeUnit 提供时间格式
     * @return 该格式的时间差 to 绝对值
     */
    default long diffAbs(DateTime233 other, TimeUnit timeUnit) {
        return Math.abs(diff(other, timeUnit));
    }


}
