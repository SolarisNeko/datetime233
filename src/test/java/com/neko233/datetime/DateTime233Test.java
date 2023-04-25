package com.neko233.datetime;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * @author SolarisNeko
 * Date on 2023-04-23
 */
public class DateTime233Test {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";


    @Test
    public void test_sync_jdk_dateTime() {
        DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");
        LocalDateTime of1 = LocalDateTime.of(2010, 1, 1, 0, 0, 0);

        for (int i = 0; i < 367; i++) {
            DateTime233 dateTime233 = of.plusDays(i);
            String jdkDateTimeString = of1.plusDays(i)
                    .format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            assertEquals(jdkDateTimeString, dateTime233.toString("yyyy-MM-dd HH:mm:ss"));
        }
    }


    @Test
    public void test_sync_jdk_weekday() {
        DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");
        LocalDateTime of1 = LocalDateTime.of(2010, 1, 1, 0, 0, 0);

        for (int i = 0; i < 367; i++) {
            DateTime233 dateTime233 = of.plusDays(i);
            DayOfWeek dayOfWeek = of1.plusDays(i)
                    .getDayOfWeek();

            int jdkWeekDay = dayOfWeek.getValue();
            int weekDay = dateTime233.weekDay();

            if (jdkWeekDay != weekDay) {
                String format = String.format("jdkWeek = %s, dateTime233 week = %s, dateTime = %s", jdkWeekDay, weekDay, dateTime233);
                System.err.println(format);
                Assert.fail();
            }

        }
    }

    @Test
    public void test_special_format_1() {
        DateTime233 of = DateTime233.of("2010/01/01", "yyyy/MM/dd");
        assertEquals("2010-01-01 00:00:00", of.toString());
    }

    @Test
    public void test_special_format_2() {
        DateTime233 of2 = DateTime233.of("2010.01.01", "yyyy,MM,dd");
        assertEquals("2010-01-01 00:00:00", of2.toString());
    }

    @Test
    public void test_special_format_3() {
        DateTime233 of3 = DateTime233.of("2010,01,01", "yyyy.MM.dd");
        assertEquals("2010-01-01 00:00:00", of3.toString());
    }


    @Test
    public void test_weekday_1() {
        DateTime233 of = DateTime233.of("2010-01-31", "yyyy-MM-dd");
        assertEquals(7, of.weekDay());
    }


    @Test
    public void test_weekday_2() {
        // 2023-12-31
        DateTime233 of = DateTime233.of(1703952000000L);

        assertEquals(7, of.weekDay());
    }

    @Test
    public void gmtOffset() {
        int zoneId = DateTime233.now().gmtZoneId();
        assertEquals(8, zoneId);
    }

    @Test
    public void year() {
        int year = DateTime233.now().year();
        assertEquals(year, LocalDateTime.now().getYear());
    }

    @Test
    public void year_set() {
        DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");
        DateTime233 year1 = of.year(2020);

        assertEquals(2020, year1.year());
        assertEquals("2020-01-01", year1.toString("yyyy-MM-dd"));
    }

    @Test
    public void month() {
        int month = DateTime233.now().month();
        assertEquals(month, LocalDateTime.now().getMonth().getValue());
    }

    @Test
    public void month_set_ok() {
        DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");
        DateTime233 dateTime = of.month(11);

        assertEquals(11, dateTime.month());
        assertEquals("2010-11-01", dateTime.toString("yyyy-MM-dd"));
    }

    @Test
    public void month_set_error() {
        DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");

        assertThrows(IllegalArgumentException.class, () -> {
            DateTime233 dateTime = of.month(13);
        });

    }


    @Test
    public void day() {
        int day = DateTime233.now().day();
        assertEquals(day, LocalDateTime.now().getDayOfMonth());
    }

    @Test
    public void day_set() {
        DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");
        DateTime233 dateTime = of.day(10);

        assertEquals(10, dateTime.day());
        assertEquals("2010-01-10", dateTime.toString("yyyy-MM-dd"));
    }


    @Test
    public void hour() {
        int hour = DateTime233.now().hour();
        assertEquals(hour, LocalDateTime.now().getHour());
    }


    @Test
    public void hour_set() {
        DateTime233 of = DateTime233.of("2010-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        DateTime233 dateTime = of.hour(10);

        assertEquals(10, dateTime.hour());
        assertEquals("2010-01-01 10:00:00", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }


    @Test
    public void minute() {
        int minute = DateTime233.now().minute();
        assertEquals(minute, LocalDateTime.now().getMinute());
    }


    @Test
    public void minute_set() {
        DateTime233 of = DateTime233.of("2010-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        DateTime233 dateTime = of.minute(10);

        assertEquals(10, dateTime.minute());
        assertEquals("2010-01-01 00:10:00", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }


    @Test
    public void second() {
        int second = DateTime233.now().second();
        assertEquals(second, LocalDateTime.now().getSecond());
    }


    @Test
    public void second_set() {
        DateTime233 of = DateTime233.of("2010-01-01 00:00:00", "yyyy-MM-dd HH:mm:ss");
        DateTime233 dateTime = of.second(10);

        assertEquals(10, dateTime.second());
        assertEquals("2010-01-01 00:00:10", dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }


    @Test
    public void plusYears() {
        DateTime233 dateTime = DateTime233.now().plusYears(1);
        assertEquals(getDateTimeString(LocalDateTime.now().plusYears(1)), dateTime.toString());
    }

    @Test
    public void minusYears() {
        DateTime233 dateTime = DateTime233.now().minusYears(1);
        if (dateTime.isLearYear()) {
            dateTime = dateTime.plusDays(1);
        }
        assertEquals(getDateTimeString(LocalDateTime.now().minusYears(1)), dateTime.toString());
    }

    @Test
    public void plusMonths() {
        DateTime233 dateTime = DateTime233.now().plusMonths(1);
        assertEquals(getDateTimeString(LocalDateTime.now().plusMonths(1)), dateTime.toString());
    }

    @Test
    public void minusMonths() {
        DateTime233 dateTime = DateTime233.now().minusMonths(1);
        assertEquals(getDateTimeString(LocalDateTime.now().minusMonths(1)), dateTime.toString());
    }

    @Test
    public void plusDays() {
        DateTime233 dateTime = DateTime233.now().plusDays(1);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(getDateTimeString(now.plusDays(1)), dateTime.toString());
    }


    @Test
    public void minusDays() {
        DateTime233 dateTime = DateTime233.now().minusDays(1);
        assertEquals(getDateTimeString(LocalDateTime.now().minusDays(1)), dateTime.toString());
    }

    @Test
    public void plusHours() {
        DateTime233 dateTime = DateTime233.now().plusHours(1);
        assertEquals(getDateTimeString(LocalDateTime.now().plusHours(1)), dateTime.toString());
    }

    @Test
    public void minusHours() {
        DateTime233 dateTime = DateTime233.now().minusHours(1);
        assertEquals(getDateTimeString(LocalDateTime.now().minusHours(1)), dateTime.toString());
    }

    @Test
    public void plusMinutes() {
        DateTime233 dateTime = DateTime233.now().plusMinutes(1);
        assertEquals(getDateTimeString(LocalDateTime.now().plusMinutes(1)), dateTime.toString());
    }

    @Test
    public void minusMinutes() {
        DateTime233 dateTime = DateTime233.now().minusMinutes(1);
        assertEquals(getDateTimeString(LocalDateTime.now().minusMinutes(1)), dateTime.toString());
    }

    @Test
    public void plusSeconds() {
        DateTime233 dateTime = DateTime233.now().plusSeconds(1);
        assertEquals(getDateTimeString(LocalDateTime.now().plusSeconds(1)), dateTime.toString());
    }

    @NotNull
    private static String getDateTimeString(LocalDateTime dateTime) {
        return dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    @Test
    public void toLocalDateTime() {
        DateTime233 now = DateTime233.now();
        LocalDateTime localDateTime = now.toLocalDateTime();

        String format = localDateTime.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        assertEquals(format, now.toString());
    }

    @Test
    public void toDate() {
        DateTime233 now = DateTime233.now();
        Date date = now.toDate();
        String format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
        assertEquals(format, now.toString());
    }

    @Test
    public void getWeekday_Monday_1() {
        // 周一
        DateTime233 of = DateTime233.of("2023-01-09 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(1, weekDay);
    }

    @Test
    public void getWeekday_Tuesday_2() {
        // 周二
        DateTime233 of = DateTime233.of("2023-01-10 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(2, weekDay);
    }

    @Test
    public void getWeekday_Wednesday_3() {
        // 周三
        DateTime233 of = DateTime233.of("2023-01-11 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(3, weekDay);
    }

    @Test
    public void getWeekday_Thursday_4() {
        // 周四
        DateTime233 of = DateTime233.of("2023-01-12 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(4, weekDay);
    }

    @Test
    public void getWeekday_Friday_5() {
        // 周五
        DateTime233 of = DateTime233.of("2023-01-13 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(5, weekDay);
    }

    @Test
    public void getWeekday_Saturday_6() {
        // 周六
        DateTime233 of = DateTime233.of("2023-01-14 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(6, weekDay);
    }

    @Test
    public void getWeekday_Sunday_7() {
        // 周日
        DateTime233 of = DateTime233.of("2023-01-15 09:00:00", "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.weekDay();
        assertEquals(7, weekDay);
    }

    @Test
    public void getWeekday_sameTo_jdk_LocalDateTime() {
        int weekDay = DateTime233.now().weekDay();
        assertEquals(LocalDateTime.now().getDayOfWeek().getValue(), weekDay);
    }

    @Test
    public void isEquals() {
        DateTime233 one = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        assertEquals(true, one.isEquals(two));
    }

    @Test
    public void isAfter() {
        DateTime233 one = DateTime233.of("2023-01-02", "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        assertEquals(true, one.isAfter(two));
    }

    @Test
    public void isBefore() {
        DateTime233 one = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-02", "yyyy-MM-dd");
        assertEquals(true, one.isBefore(two));
    }

    @Test
    public void diff() {
        DateTime233 one = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-02", "yyyy-MM-dd");
        assertEquals(-1, one.diff(two, TimeUnit.DAYS));
    }

    @Test
    public void diffAbs() {
        DateTime233 one = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-02", "yyyy-MM-dd");
        assertEquals(1, one.diffAbs(two, TimeUnit.DAYS));
    }

}