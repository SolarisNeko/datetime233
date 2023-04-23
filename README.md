# DateTime233



## Introduce | 简介

This is a zero-start design DateTime Utils, used to flux DateTime233 API.

从 0 开始设计的 DateTime 工具, flux-style DateTime233 API.

## Use
### maven
```xml
<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>datetime233</artifactId>
    <version>0.0.2</version>
</dependency>
```
### gradle
```kotlin
implementation("com.neko233:datetime233:0.0.2")
```

## Support JDK | JDK 版本支持

latest support version: 

JDK 8 = 0.0.2

JDK 11 = 0.0.2

JDK 17 = 0.0.2


### 介绍

DateTime233 is a flux-style utils, He can connect LocalDateTime/Date seamlessly.

无缝衔接 JDK LocalDateTime 和 Date

License 为 Apache2.0

## Download

### Maven

```xml

<dependency>
    <groupId>com.neko233</groupId>
    <artifactId>datetime233</artifactId>
    <version>0.0.2</version>
</dependency>

```

### Gradle

```groovy
implementation group: 'com.neko233', name: 'datetime233', version: '0.0.2'
```

## 初衷 / 痛点

Enough of DateTime/Date, to do a lot of their own packaging. And there are few functions that are supported.

受够了 LocalDateTime, Date 的局限性, 要做大量自己的封装. 并且支持的功能还很少. 


# Code 

## Java
```java
package com.neko233.datetime;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * @author SolarisNeko
 * Date on 2023-04-23
 */
public class DateTime233Test {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void test_flux() {
        String string = DateTime233.now()
                .plusHours(1)
                .plusYears(1)
                .toString();
        System.out.println(string);


        long msFrom1970 = DateTime233.now()
                .plusHours(1)
                .plusYears(1)
                .toMsFrom1970();
        System.out.println(msFrom1970);
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
        assertEquals(getDateTimeString(LocalDateTime.now().plusDays(1)), dateTime.toString());
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

}
```


