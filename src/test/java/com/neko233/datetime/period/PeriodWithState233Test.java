package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author SolarisNeko
 * Date on 2023-04-25
 */
public class PeriodWithState233Test {

    @Test
    public void test_isInPeriod() {
        DateTime233 of = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 end = DateTime233.of("2024-01-01", "yyyy-MM-dd");
        Period233 between = Period233.between(of, end);

        PeriodWithState233 calc = between.getPeriodState(System.currentTimeMillis());

        // 1672502400000L ms = 2023-01-01 00:00:00
        Assert.assertEquals(1672502400000L, calc.getStartMs());
        // 1704038400000 ms = 2024-01-01 00:00:00
        Assert.assertEquals(1704038400000L, calc.getEndMs());
        // 1704038400000L ms = 2024-01-01 00:00:00
        Assert.assertEquals(1704038400000L, calc.getExpireMs());
    }

    @Test
    public void testIsInPeriod() {
    }

}