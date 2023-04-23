package com.neko233.datetime;

import com.neko233.datetime.constant.Month233;

/**
 * @author SolarisNeko
 * Date on 2023-04-23
 */
public class DateTimeChecker233 {


    /**
     * 检查是否正确月份
     *
     * @param month 月份
     */
    public static void isOkMonth(int month) {
        if (month < Month233.MIN_MONTH) {
            throw new IllegalArgumentException("can not month < " + Month233.MIN_MONTH);
        }
        if (month > Month233.MAX_MONTH) {
            throw new IllegalArgumentException("can not month > " + Month233.MAX_MONTH);
        }
    }

    /**
     * 日子设置是否 ok
     *
     * @param year  年
     * @param month 月
     * @param day   日 toSet
     */
    public static void isDayOk(int year, int month, int day) {
        isOkMonth(month);

        if (DateTime233.isLeapYear(year) && month == 2) {
            if (0 < day && day < 29) {
                return;
            }
        }
        int maxDay = Month233.DAYS_IN_MONTH_ARRAY[month];
        if (0 < day && day <= maxDay) {
            return;
        }
        String exceptionMsg = String.format("error day! year = %d, month = %d, day = %d", year, month, day);
        throw new IllegalArgumentException(exceptionMsg);
    }

    public static void isHourOk(int hour) {
        if (0 <= hour && hour <= 24) {
            return;
        }
        String exceptionMsg = String.format("error set hour! hour = %s", hour);
        throw new IllegalArgumentException(exceptionMsg);
    }

    public static void isSecondOk(int second) {
        if (0 <= second && second <= 60) {
            return;
        }
        String exceptionMsg = String.format("error set second! second = %s", second);
        throw new IllegalArgumentException(exceptionMsg);
    }

    public static void isMinuteOk(int minute) {
        if (0 <= minute && minute <= 60) {
            return;
        }
        String exceptionMsg = String.format("error set minute! minute = %s", minute);
        throw new IllegalArgumentException(exceptionMsg);
    }
}
