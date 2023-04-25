package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author SolarisNeko
 * Date on 2023-04-25
 */
public class Period233Test {



    @Test
    public void getAllWeekends() {
        DateTime233 of = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 end = DateTime233.of("2024-01-01", "yyyy-MM-dd");
        Period233 between = Period233.between(of, end);


        List<String> collect = between.getAllWeekends()
                .stream()
                .map(DateTime233::toString)
                .collect(Collectors.toList());


    }

    @Test
    public void step() {
        DateTime233 of = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 end = DateTime233.of("2024-01-01", "yyyy-MM-dd");
        Period233 between = Period233.between(of, end);

        PeriodDaddy233 periodDaddy = between.splitByFixTimeStep(1, TimeUnit.DAYS);
        int childPeriodCount = periodDaddy.getChildPeriodCount();

        Assert.assertEquals(365, childPeriodCount);
    }

    @Test
    public void dynamic() {
        DateTime233 of = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 end = DateTime233.of("2024-01-01", "yyyy-MM-dd");
        Period233 between = Period233.between(of, end);

        PeriodDaddy233 periodsChain = between.generateDynamicPeriod((count) -> {
            return count * TimeUnit.DAYS.toMillis(1);
        }, (count) -> {
            return 0L;
        });
        periodsChain.forEach(period -> {
            DateTime233 startDt = DateTime233.of(period.getStartMs());
            DateTime233 endDt = DateTime233.of(period.getEndMs());
            DateTime233 expireDt = DateTime233.of(period.getExpireMs());
        });
    }

    @Test
    public void step_complex() {
        DateTime233 of = DateTime233.of("2023-01-01", "yyyy-MM-dd");
        DateTime233 end = DateTime233.of("2024-01-01", "yyyy-MM-dd");
        Period233 between = Period233.between(of, end);

        PeriodDaddy233 periodsDad = between.splitByFixTimeStep(30, TimeUnit.DAYS, 30, TimeUnit.DAYS);
        periodsDad.forEach(period -> {
            DateTime233 startDt = DateTime233.of(period.getStartMs());
            DateTime233 endDt = DateTime233.of(period.getEndMs());
            DateTime233 expireDt = DateTime233.of(period.getExpireMs());
        });
    }


}