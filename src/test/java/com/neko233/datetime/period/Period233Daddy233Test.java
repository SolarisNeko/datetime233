package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko
 * Date on 2023-04-25
 */
public class Period233Daddy233Test {


    @Test
    public void isInPeriod() {
        DateTime233 of = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 end = DateTime233.of("2024-01-01", "yyyy-MM-dd");
        Period233 between = Period233.between(of, end);

        PeriodDad233 periodsDad = between.toPeriodDad(30, TimeUnit.DAYS);

        int childPeriodCount = periodsDad.getChildPeriodCount();
        Assert.assertEquals(13, childPeriodCount);

        int minPeriodIndex = periodsDad.getMinPeriodIndex();
        Assert.assertEquals(1, minPeriodIndex);

        int maxPeriodIndex = periodsDad.getMaxPeriodIndex();
        Assert.assertEquals(13, maxPeriodIndex);


    }

    @Test
    public void testIsInPeriod() {
    }


}