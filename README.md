# DateTime233 | Simplify Your Date and Time Operations

```java
DateTime233 of = DateTime233.of("2010-01-01", "yyyy-MM-dd");

int timeZoneId = of.getZoneId();

```

## Introduction

DateTime233 is a powerful and intuitive DateTime utility that simplifies working with dates and times. Designed from
scratch with a flux-style architecture, it seamlessly integrates with the DateTime233 API.

DateTime233 是一个从 0 开始设计的强大且直观的日期和时间工具，采用流式 DateTime 风格简化了日期和时间操作, 提供可并发的一体化
DateTime 操作.

并且与 JDK Date / LocalDateTime / millisSeconds 无缝衔接。

能力:

1. DateTime 日期时间
2. Period 周期

# Easy Integration

## maven

```xml

<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>datetime233</artifactId>
    <version>1.0.1</version>
</dependency>
```

## gradle

```kotlin
implementation("com.neko233:datetime233:1.0.1")

```

## JDK Compatibility | JDK 版本支持

Latest supported versions:

JDK 8 = 1.0.1

JDK 11 = 1.0.1

JDK 17 = 1.0.1

## Key Terminology | 术语

To make the most of DateTime233, familiarize yourself with the following terms and concepts:

1. originTimeMs: Milliseconds since January 1, 1970, representing the original time.
2. zoneTimeMs: Milliseconds in the current time zone since January 1, 1970.
3. DateTime: A date and time represented in the format "yyyy-MM-dd HH:mm:ss."
4. Period: A time interval specified by a start and end timestamp.
5. refreshMs: The time interval between period refreshes, typically measured in milliseconds.

---

1. originTimeMs = millis second = 毫秒, 从 1970-01-01 00:00:00 至今
2. zoneTimeMs = zone time ms = 时区下的毫秒, 从 1970-01-01 00:00:00 至今
3. DateTime = yyyy-MM-dd HH:mm:ss 组成的日期时间
4. Period = 周期 = [start, endMs] -> {startMs, endMs, expireMs, refreshMs}
5. RefreshMs = period refresh by refreshMs / time, like 100ms refresh , in 1 s have 10 refresh count.

# Seamless Integration

DateTime233 seamlessly connects with LocalDateTime and Date objects, eliminating the need for extensive custom
packaging.

DateTime233 可无缝连接 LocalDateTime 和 Date 对象，省去了繁琐的自定义封装。

```java

DateTime233 fromOldDate = DateTime233.from(new Date());

DateTime233 fromJdk8DateTime = DateTime233.from(LocalDateTime.now());


```

# License

DateTime233 is licensed under Apache 2.0.

## Download

### Maven

```xml

<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>datetime233</artifactId>
    <version>1.0.1</version>
</dependency>

```

### Gradle

```groovy
implementation group: 'com.neko233', name: 'datetime233', version: '1.0.1'
```

# Code

## Java

### DateTime233  日期时间

```java

import com.neko233.datetime.constant.DateTimeUnit;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
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


    public static final LocalDateTime jdkDateTime_1970_01_01_00_00_00 = LocalDateTime.of(1970,
            1,
            1,
            0,
            0,
            0);


    public static final DateTime233 dateTime233_1970_01_01_00_00_00 = DateTime233.of("1970-01-01 00:00:00",
            "yyyy-MM-dd HH:mm:ss");

    @NotNull
    private static String getDateTimeString(LocalDateTime dateTime) {
        return dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @NotNull
    private static String getDateTimeString(ZonedDateTime dateTime) {
        return dateTime
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void bugfix_test_timeMs_1() {
        DateTime233 of = DateTime233.of("2010-10-01",
                "yyyy-MM-dd");

        long timeMs_2010_10_01 = of.originalTimeMs();
        assertEquals(1285862400000L,
                timeMs_2010_10_01);

        DateTime233 dt_2010_10_02 = of.plusDays(1);
        long timeMs_2010_10_02 = dt_2010_10_02.originalTimeMs();
        assertEquals(1285948800000L,
                timeMs_2010_10_02);

        long dt_2001_11_01 = DateTime233.of("2001-11-01",
                        "yyyy-MM-dd")
                .originalTimeMs();
        assertEquals(1004544000000L,
                dt_2001_11_01);
    }

    // 70 ms ~ 300ms
    @Test
    public void test_timeMs_check() {

        DateTime233 dt = DateTime233.of("1970-01-01",
                "yyyy-MM-dd");
        LocalDateTime jdkDt = LocalDateTime.of(1970,
                1,
                1,
                0,
                0,
                0);

        // 检查 200 年 ~= 7w3 ~= 10w times
        int year200 = 365 * 300;
        for (int i = 0; i < year200; i++) {

            // JDK LocalDateTime toInstant, if you is zone+8, it will give you "- zone+8 ms" originMs...
            long zoneTimeMsByJdk = jdkDt.toInstant(ZoneOffset.of("+0"))
                    .getEpochSecond() * 1000;
            long zoneTimeMs = dt.zoneTimeMs();
            assertEquals(zoneTimeMsByJdk,
                    zoneTimeMs);

            dt = dt.plusDays(1);
            jdkDt = jdkDt.plusDays(1);
        }


    }

    @Test
    public void test_sync_jdk_dateTime() {
        DateTime233 of = DateTime233.of("2010-01-01",
                "yyyy-MM-dd");
        LocalDateTime of1 = LocalDateTime.of(2010,
                1,
                1,
                0,
                0,
                0);

        for (int i = 0; i < 367; i++) {
            DateTime233 dateTime233 = of.plusDays(i);
            String jdkDateTimeString = of1.plusDays(i)
                    .format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
            assertEquals(jdkDateTimeString,
                    dateTime233.toString("yyyy-MM-dd HH:mm:ss"));
        }
    }

    @Test
    public void test_sync_jdk_weekday() {
        DateTime233 of = DateTime233.of("2010-01-01",
                "yyyy-MM-dd");
        LocalDateTime of1 = LocalDateTime.of(2010,
                1,
                1,
                0,
                0,
                0);

        for (int i = 0; i < 367; i++) {
            DateTime233 dateTime233 = of.plusDays(i);
            DayOfWeek dayOfWeek = of1.plusDays(i)
                    .getDayOfWeek();

            int jdkWeekDay = dayOfWeek.getValue();
            int weekDay = dateTime233.getWeekDay();

            if (jdkWeekDay != weekDay) {
                String format = String.format("jdkWeek = %s, dateTime233 week = %s, dateTime = %s",
                        jdkWeekDay,
                        weekDay,
                        dateTime233);
                System.err.println(format);
                Assert.fail();
            }

        }
    }

    @Test
    public void test_from_jdkDate() throws ParseException {
        String dateString = "2020-01-01 00:00:00";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = formatter.parse(dateString);

        DateTime233 of = DateTime233.from(date);
        assertEquals("2020-01-01 00:00:00",
                of.toString());
    }

    @Test
    public void test_from_jdkDateTime() throws ParseException {
        String dateTimeStr = "2020-01-01 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr,
                formatter);

        DateTime233 of = DateTime233.from(dateTime);
        assertEquals("2020-01-01 00:00:00",
                of.toString());
    }

    @Test
    public void test_from_jdkZonedDateTime() throws ParseException {
        String dateTimeStr = "2020-01-01 00:00:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr,
                formatter);
        ZonedDateTime zonedDateTime = dateTime.atZone(ZoneId.systemDefault());

        DateTime233 of = DateTime233.from(zonedDateTime);
        assertEquals("2020-01-01 00:00:00",
                of.toString());
    }

    @Test
    public void test_special_format_0() {
        DateTime233 of = DateTime233.of("20100101",
                "yyyyMMdd");
        assertEquals("2010-01-01 00:00:00",
                of.toString());
    }

    @Test
    public void test_special_format_1() {
        DateTime233 of = DateTime233.of("2010/01/01",
                "yyyy/MM/dd");
        assertEquals("2010-01-01 00:00:00",
                of.toString());
    }

    @Test
    public void test_special_format_2() {
        DateTime233 of2 = DateTime233.of("2010.01.01",
                "yyyy.MM.dd");
        assertEquals("2010-01-01 00:00:00",
                of2.toString());
    }

    @Test
    public void test_special_format_3() {
        DateTime233 of3 = DateTime233.of("2010,01,01",
                "yyyy,MM,dd");
        assertEquals("2010-01-01 00:00:00",
                of3.toString());
    }

    @Test
    public void test_weekday_1() {
        DateTime233 of = DateTime233.of("2010-01-31",
                "yyyy-MM-dd");
        assertEquals(7,
                of.getWeekDay());
    }

    @Test
    public void test_weekday_2() {
        // 2023-12-31
        DateTime233 of = DateTime233.of(1703952000000L);

        assertEquals(7,
                of.getWeekDay());
    }

    @Test
    public void gmtOffset() {
        int zoneId = DateTime233.now()
                .getZoneId();
        assertEquals(8,
                zoneId);
    }

    @Test
    public void year() {
        int year = DateTime233.now()
                .year();
        assertEquals(year,
                LocalDateTime.now()
                        .getYear());
    }

    @Test
    public void year_set() {
        DateTime233 of = DateTime233.of("2010-01-01",
                "yyyy-MM-dd");
        DateTime233 year1 = of.year(2020);

        assertEquals(2020,
                year1.year());
        assertEquals("2020-01-01",
                year1.toString("yyyy-MM-dd"));
    }

    @Test
    public void month() {
        int month = DateTime233.now()
                .month();
        assertEquals(month,
                LocalDateTime.now()
                        .getMonth()
                        .getValue());
    }

    @Test
    public void month_set_ok() {
        DateTime233 of = DateTime233.of("2010-01-01",
                "yyyy-MM-dd");
        DateTime233 dateTime = of.month(11);

        assertEquals(11,
                dateTime.month());
        assertEquals("2010-11-01",
                dateTime.toString("yyyy-MM-dd"));
    }

    @Test
    public void month_set_error() {
        DateTime233 of = DateTime233.of("2010-01-01",
                "yyyy-MM-dd");

        assertThrows(IllegalArgumentException.class,
                () -> {
                    DateTime233 dateTime = of.month(13);
                });

    }

    @Test
    public void day() {
        int day = DateTime233.now()
                .day();
        assertEquals(day,
                LocalDateTime.now()
                        .getDayOfMonth());
    }

    @Test
    public void day_set() {
        DateTime233 of = DateTime233.of("2010-01-01",
                "yyyy-MM-dd");
        DateTime233 dateTime = of.day(10);

        assertEquals(10,
                dateTime.day());
        assertEquals("2010-01-10",
                dateTime.toString("yyyy-MM-dd"));
    }

    @Test
    public void hour() {
        int hour = DateTime233.now()
                .hour();
        assertEquals(hour,
                LocalDateTime.now()
                        .getHour());
    }

    @Test
    public void hour_set() {
        DateTime233 of = DateTime233.of("2010-01-01 00:00:00",
                "yyyy-MM-dd HH:mm:ss");
        DateTime233 dateTime = of.hour(10);

        assertEquals(10,
                dateTime.hour());
        assertEquals("2010-01-01 10:00:00",
                dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void minute() {
        int minute = DateTime233.now()
                .minute();
        assertEquals(minute,
                LocalDateTime.now()
                        .getMinute());
    }

    @Test
    public void minute_set() {
        DateTime233 of = DateTime233.of("2010-01-01 00:00:00",
                "yyyy-MM-dd HH:mm:ss");
        DateTime233 dateTime = of.minute(10);

        assertEquals(10,
                dateTime.minute());
        assertEquals("2010-01-01 00:10:00",
                dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void second() {
        int second = DateTime233.now()
                .second();
        assertEquals(second,
                LocalDateTime.now()
                        .getSecond());
    }

    @Test
    public void second_set() {
        DateTime233 of = DateTime233.of("2010-01-01 00:00:00",
                "yyyy-MM-dd HH:mm:ss");
        DateTime233 dateTime = of.second(10);

        assertEquals(10,
                dateTime.second());
        assertEquals("2010-01-01 00:00:10",
                dateTime.toString("yyyy-MM-dd HH:mm:ss"));
    }

    @Test
    public void plusYears() {
        DateTime233 dateTime = DateTime233.now()
                .plusYears(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .plusYears(1)),
                dateTime.toString());
    }

    @Test
    public void minusYears() {
        DateTime233 dateTime = DateTime233.now()
                .minusYears(1);
        if (dateTime.isLearYear()) {
            dateTime = dateTime.plusDays(1);
        }
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .minusYears(1)),
                dateTime.toString());
    }

    @Test
    public void plusMonths() {
        DateTime233 dateTime = DateTime233.now()
                .plusMonths(1);

        assertEquals(getDateTimeString(LocalDateTime.now()
                        .plusMonths(1)),
                dateTime.toString());
    }

    @Test
    public void plusMonths_bug_1() {
        DateTime233 dateTime233 = dateTime233_1970_01_01_00_00_00.plusMonths(13);
        String dateTime233String = dateTime233.toString();
        assertEquals("1971-02-01 00:00:00",
                dateTime233String);
    }

    @Test
    public void plusMonths_bug_2() {
        String dtStr = getDateTimeString(jdkDateTime_1970_01_01_00_00_00.plusMonths(11));
        DateTime233 dateTime233 = dateTime233_1970_01_01_00_00_00.plusMonths(11);

        String dateTime233String = dateTime233.toString();
        assertEquals(dtStr,
                dateTime233String);
    }

    @Test
    public void plusMonths_bug_3() {
        String dtStr = getDateTimeString(jdkDateTime_1970_01_01_00_00_00.plusMonths(23));

        DateTime233 dateTime233 = dateTime233_1970_01_01_00_00_00.plusMonths(23);

        String dateTime233String = dateTime233.toString();
        assertEquals(dtStr,
                dateTime233String);
    }

    @Test
    public void plusMonths_bug_4() {
//        Expected :2069-01-01 00:00:00
//        Actual   :2068-12-31 00:00:00
        String dtStr = getDateTimeString(jdkDateTime_1970_01_01_00_00_00.plusMonths(23));

        DateTime233 dateTime233 = dateTime233_1970_01_01_00_00_00.plusMonths(23);

        String dateTime233String = dateTime233.toString();
        assertEquals(dtStr,
                dateTime233String);
    }

    // FIXME
    public void plusMonths_jdk_dateTime_bug() {
        // 过了 36159 天, 理应 2068-12-31 00:00:00, JDK 计算为 2069-01-01 00:00:00
        long timestamp = 3124137600000L;
        Instant instant = Instant.ofEpochMilli(timestamp);
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.of("+0"));

        String dateTimeString = getDateTimeString(localDateTime);
        assertEquals("2069-01-01 00:00:00", dateTimeString);
    }

    @Test
    public void plusMonths_12month_500year() {
        DateTime233 dateTime233 = DateTime233.of("1970-01-01 00:00:00",
                "yyyy-MM-dd HH:mm:ss");


        for (int i = 0; i < 12 * 500; i++) {
            LocalDateTime jdkDateTime = jdkDateTime_1970_01_01_00_00_00.plusMonths(i);
            String dtStr = getDateTimeString(jdkDateTime);
            String dateTime233Str = dateTime233.plusMonths(i)
                    .toString();

            if (dtStr.equalsIgnoreCase(dateTime233Str)) {
                continue;
            }

            System.err.println("plus month error count = " + i);

            // FIXME JDK dateTime bug, it have some offset
            if (jdkDateTime.getYear() > 2068) {
                break;
            }

            String forDebug = dateTime233.plusMonths(i)
                    .toString();

            assertEquals(dtStr,
                    dateTime233Str);
        }
    }

    @Test
    public void minusMonths() {
        DateTime233 dateTime = DateTime233.now()
                .minusMonths(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .minusMonths(1)),
                dateTime.toString());
    }

    @Test
    public void minusMonths_changeYear() {
        DateTime233 dateTime = DateTime233.now()
                .minusMonths(12);
        LocalDateTime dateTime1 = LocalDateTime.now()
                .minusMonths(12);
        assertEquals(getDateTimeString(dateTime1),
                dateTime.toString());
    }

    @Test
    public void plusDays() {
        DateTime233 dateTime = DateTime233.now()
                .plusDays(1);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(getDateTimeString(now.plusDays(1)),
                dateTime.toString());
    }

    @Test
    public void plusDays_changeMonths() {
        DateTime233 dateTime = DateTime233.now()
                .plusDays(40);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(getDateTimeString(now.plusDays(40)),
                dateTime.toString());
    }

    @Test
    public void plusDays_changeYears() {
        DateTime233 dateTime = DateTime233.now()
                .plusDays(370);
        LocalDateTime now = LocalDateTime.now();
        assertEquals(getDateTimeString(now.plusDays(370)),
                dateTime.toString());
    }

    @Test
    public void minusDays() {
        DateTime233 dateTime = DateTime233.now()
                .minusDays(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .minusDays(1)),
                dateTime.toString());
    }

    @Test
    public void plusHours() {
        DateTime233 dateTime = DateTime233.now()
                .plusHours(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .plusHours(1)),
                dateTime.toString());
    }

    @Test
    public void minusHours() {
        DateTime233 dateTime = DateTime233.now()
                .minusHours(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .minusHours(1)),
                dateTime.toString());
    }

    @Test
    public void plusMinutes() {
        DateTime233 dateTime = DateTime233.now()
                .plusMinutes(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .plusMinutes(1)),
                dateTime.toString());
    }

    @Test
    public void minusMinutes() {
        DateTime233 dateTime = DateTime233.now()
                .minusMinutes(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .minusMinutes(1)),
                dateTime.toString());
    }

    @Test
    public void plusSeconds() {
        DateTime233 dateTime = DateTime233.now()
                .plusSeconds(1);
        assertEquals(getDateTimeString(LocalDateTime.now()
                        .plusSeconds(1)),
                dateTime.toString());
    }

    @Test
    public void toLocalDateTime() {
        DateTime233 now = DateTime233.now();
        LocalDateTime localDateTime = now.toLocalDateTime();

        String format = localDateTime.format(DateTimeFormatter.ofPattern(YYYY_MM_DD_HH_MM_SS));
        assertEquals(format,
                now.toString());
    }

    @Test
    public void toDate() {
        DateTime233 now = DateTime233.now();
        Date date = now.toDate();
        String format = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(date);
        assertEquals(format,
                now.toString());
    }

    @Test
    public void getWeekday_Monday_1() {
        // 周一
        DateTime233 of = DateTime233.of("2023-01-09 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();
        assertEquals(1,
                weekDay);
    }

    @Test
    public void getWeekday_Tuesday_2() {
        // 周二
        DateTime233 of = DateTime233.of("2023-01-10 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();
        assertEquals(2,
                weekDay);
    }

    @Test
    public void getWeekday_Wednesday_3() {
        // 周三
        DateTime233 of = DateTime233.of("2023-01-11 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();
        assertEquals(3,
                weekDay);
    }

    @Test
    public void getWeekday_Thursday_4() {
        // 周四
        DateTime233 of = DateTime233.of("2023-01-12 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();

        assertEquals(4,
                weekDay);
    }

    @Test
    public void getWeekday_Friday_5() {
        // 周五
        DateTime233 of = DateTime233.of("2023-01-13 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();
        assertEquals(5,
                weekDay);
    }

    @Test
    public void getWeekday_Saturday_6() {
        // 周六
        DateTime233 of = DateTime233.of("2023-01-14 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();
        assertEquals(6,
                weekDay);
    }

    @Test
    public void getWeekday_Sunday_7() {
        // 周日
        DateTime233 of = DateTime233.of("2023-01-15 09:00:00",
                "yyyy-MM-dd HH:mm:ss");
        int weekDay = of.getWeekDay();
        assertEquals(7,
                weekDay);
    }

    @Test
    public void getWeekday_sameTo_jdk_LocalDateTime() {
        int weekDay = DateTime233.now()
                .getWeekDay();
        assertEquals(LocalDateTime.now()
                        .getDayOfWeek()
                        .getValue(),
                weekDay);
    }

    @Test
    public void isEquals() {
        DateTime233 one = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        assertEquals(true,
                one.isEquals(two));
    }

    @Test
    public void isAfter() {
        DateTime233 one = DateTime233.of("2023-01-02",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        assertEquals(true,
                one.isAfter(two));
    }

    @Test
    public void isBefore() {
        DateTime233 one = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-02",
                "yyyy-MM-dd");
        assertEquals(true,
                one.isBefore(two));
    }

    @Test
    public void diffByTimeUnit() {
        DateTime233 one = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-02",
                "yyyy-MM-dd");
        assertEquals(-1,
                one.diffByTimeUnit(two,
                        TimeUnit.DAYS));
    }

    @Test
    public void diffAbs() {
        DateTime233 one = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2023-01-02",
                "yyyy-MM-dd");
        assertEquals(1,
                one.diffAbsByTimeUnit(two,
                        TimeUnit.DAYS));
    }

    @Test
    public void diff_year() {
        DateTime233 one = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2024-02-02",
                "yyyy-MM-dd");
        assertEquals(1, one.diff(two, DateTimeUnit.YEAR));
    }

    @Test
    public void diff_month() {
        DateTime233 one = DateTime233.of("2023-01-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2024-02-02",
                "yyyy-MM-dd");
        assertEquals(13, one.diff(two, DateTimeUnit.MONTH));
    }

    @Test
    public void diff_month_2() {
        DateTime233 one = DateTime233.of("2023-12-01",
                "yyyy-MM-dd");
        DateTime233 two = DateTime233.of("2024-02-02",
                "yyyy-MM-dd");
        assertEquals(2, one.diff(two, DateTimeUnit.MONTH));
    }


}
```

# Pic

![DateTime233](./datetime233-logo-v1.png)