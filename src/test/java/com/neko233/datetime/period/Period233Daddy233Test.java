package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import org.junit.Assert;
import org.junit.Test;

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

        DateTime233 of1 = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        long currentMs = of1.originalTimeMs();
        long currentZoneMs = of1.zoneTimeMs();

        PeriodWithState233 calc = between.getPeriodState(currentMs);

        Assert.assertTrue(calc.isInPeriod(currentMs));
        Assert.assertTrue(calc.isInPeriod(currentZoneMs));
    }

    @Test
    public void testIsInPeriod() {
    }


}