package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import com.neko233.datetime.api.PeriodApi;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author SolarisNeko on 2023-04-24
 **/
@EqualsAndHashCode
@ToString
public class Period233 implements PeriodApi {

    // 开始时间
    private final long startMs;
    // 结束时间
    private final long endMs;
    // 周期过期时间
    private final long expireMs;
    // 周期内多久刷新一次时间, < 0 == 不刷新
    private final long refreshMs;

    @Override
    public long getStartMs() {
        return startMs;
    }

    @Override
    public long getEndMs() {
        return endMs;
    }

    public long getExpireMs() {
        return expireMs;
    }

    public static Period233 between(DateTime233 start, DateTime233 end) {
        return between(start, end, null);
    }

    public static Period233 between(DateTime233 start, DateTime233 end, Long expireDelayMs) {
        long startMs = start.originalTimeMs();
        long endMs = end.originalTimeMs();
        return Period233.of(startMs, endMs, expireDelayMs);
    }

    private Period233(long startMs, long endMs, Long expireMs, Long refreshMs) {
        this.startMs = startMs;
        this.endMs = endMs;
        this.expireMs = Optional.ofNullable(expireMs).orElse(endMs);
        Long realRefreshMs = Optional.ofNullable(refreshMs).orElse(-1L);
        long diffMs = endMs - startMs;
        this.refreshMs = realRefreshMs > diffMs ? -1 : realRefreshMs;
    }

    public static Period233 of(long startMs, long endMs) {
        return new Period233(startMs, endMs, null, null);
    }

    public static Period233 of(long startMs, long endMs, Long expireDelayMs) {
        Long expireMs = Optional.ofNullable(expireDelayMs).map(delayMs -> endMs + delayMs).orElse(endMs);
        return new Period233(startMs, endMs, expireMs, null);
    }

    public boolean isNotInPeriod(long currentMs) {
        return !isInPeriod(currentMs);
    }

    public boolean isInPeriod(long currentMs) {
        return startMs <= currentMs && currentMs <= endMs;
    }

    public PeriodWithState233 getPeriodState(long currentMs) {
        if (isNotInPeriod(currentMs)) {
            return PeriodWithState233.error();
        }
        return PeriodWithState233.of(startMs, endMs, expireMs, refreshMs).refresh(currentMs);
    }

    /**
     * 固定时间分割
     *
     * @param timeStep 时步
     * @param timeUnit 单元
     * @return 在这个大周期中, 拆分出来的小周期
     */
    public PeriodDaddy233 splitByFixTimeStep(int timeStep, TimeUnit timeUnit) {
        return splitByFixTimeStep(timeStep, timeUnit, null, null);
    }

    /**
     * [Fixed] split by timeStep into a continuous Periods List <br>
     * 固定的时步间隔
     *
     * @param timeStep         时步
     * @param timeUnit         单位
     * @param intervalTimeStep 间隔时步
     * @param intervalTimeUnit 间隔单位
     * @return 连续的小周期
     */
    public PeriodDaddy233 splitByFixTimeStep(int timeStep, TimeUnit timeUnit, Integer intervalTimeStep, TimeUnit intervalTimeUnit) {
        List<Period233> period233List = new ArrayList<>();
        long durationMs = timeUnit.toMillis(timeStep);
        long intervalMs = 0;
        if (intervalTimeStep != null && intervalTimeUnit != null) {
            intervalMs = intervalTimeUnit.toMillis(intervalTimeStep);
        }

        long tempStartMs = startMs;
        long tempEndMs = startMs + durationMs;

        long oneStepMs = durationMs + intervalMs;

        while (tempStartMs < endMs) {
            Period233 of1 = Period233.of(tempStartMs, tempEndMs);
            period233List.add(of1);

            tempStartMs += oneStepMs;
            tempEndMs += oneStepMs;
        }
        return PeriodDaddy233.from(period233List);
    }


    /**
     * 动态生成周期 by period count (小周期的次数, from 1 )
     *
     * @param durationMsFunc 单个周期持续多久
     * @param intervalMsFunc 周期与周期之间, 间隔多久
     * @return 周期 List
     */
    public PeriodDaddy233 generateDynamicPeriod(Function<Integer, Long> durationMsFunc, Function<Integer, Long> intervalMsFunc) {
        List<Period233> period233List = new ArrayList<>();

        int count = 1;

        long tempStartMs = startMs;
        long tempEndMs = tempStartMs;

        while (tempStartMs < endMs) {
            // 根据次数, 动态生成常规间隔
            long periodDurationMs = durationMsFunc == null ? 0 : durationMsFunc.apply(count);
            long intervalStepMs = intervalMsFunc == null ? 0 : intervalMsFunc.apply(count);

            if (count != 1) {
                tempStartMs += periodDurationMs;
            }
            tempEndMs += periodDurationMs;

            Period233 period233 = Period233.of(tempStartMs, tempEndMs);
            period233List.add(period233);

            tempStartMs += intervalStepMs;
            tempEndMs += intervalStepMs;

            count++;
        }
        return PeriodDaddy233.from(period233List);
    }

    @Override
    public long getRefreshMs() {
        return this.refreshMs;
    }

    @Override
    public PeriodDaddy233 toPeriodsDaddy() {
        return PeriodDaddy233.from(getPeriodsList());
    }

    @Override
    public List<Period233> getPeriodsList() {
        return Collections.singletonList(this);
    }
}
