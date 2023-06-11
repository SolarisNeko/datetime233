package com.neko233.datetime.convert;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko
 * Date on 2023-06-11
 */
public class TimeDiffConverter233Test {

    @Test
    public void convertMsByJdkUnit_1() {
        long milliseconds = 1200;

        String convertedTime = TimeDiffConverter233.convertMsToText(milliseconds);
        Assert.assertEquals("1 second 200 ms", convertedTime);
    }

    @Test
    public void convertMsByJdkUnit_minute() {
        long ms = TimeUnit.MINUTES.toMillis(10) + TimeUnit.SECONDS.toMillis(20);

        String convertedTime = TimeDiffConverter233.convertMsToText(ms);
        Assert.assertEquals("10 minute 20 second", convertedTime);
    }

    @Test
    public void convertToMilliseconds_1() {
        // 正序
        long ms = TimeDiffConverter233.convertTextToMs("100 day 10 minute");
        Assert.assertEquals(8640600000L, ms);
    }

    @Test
    public void convertToMilliseconds_2() {
        // 乱序
        long ms = TimeDiffConverter233.convertTextToMs(" 10   minute  100 day");
        Assert.assertEquals(8640600000L, ms);
    }

    @Test
    public void convertToMilliseconds_3() {
        // any
        long ms = TimeDiffConverter233.convertTextToMs("100 day 10   minute 10 ms");
        Assert.assertEquals(8640600010L, ms);
    }
}