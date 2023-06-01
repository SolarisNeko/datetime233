package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * @author SolarisNeko on 2023-04-24
 **/
@EqualsAndHashCode
@ToString
public class Period233 implements PeriodApi {

    public final AtomicBoolean isHaveDad = new AtomicBoolean(false);
    // 第几个周期
    private volatile int periodIndex;
    // 开始时间
    private final long startMs;
    // 结束时间
    private final long endMs;
    // 周期过期时间
    private final long expireMs;
    // 周期内多久刷新一次时间, < 0 == 不刷新
    private final long refreshMs;


    public static Period233 of(int periodIndex,
                               long startMs,
                               long endMs) {
        return new Period233(periodIndex, startMs, endMs, null, null);
    }

    public static Period233 of(int periodIndex,
                               long startMs,
                               long endMs,
                               Long expireDelayMs) {
        Long expireMs = Optional.ofNullable(expireDelayMs).map(delayMs -> endMs + delayMs).orElse(endMs);
        return new Period233(periodIndex, startMs, endMs, expireMs, null);
    }


    /**
     * 在两个时间点上, 构建周期
     *
     * @param start 开始
     * @param end   结束
     * @return 周期
     */
    public static Period233 between(DateTime233 start,
                                    DateTime233 end) {
        return between(start, end, null);
    }

    public static Period233 between(DateTime233 start,
                                    DateTime233 end,
                                    Long expireDelayMs) {
        long startMs = start.originalTimeMs();
        long endMs = end.originalTimeMs();
        return Period233.of(1, startMs, endMs, expireDelayMs);
    }

    private Period233(int periodIndex,
                      long startMs,
                      long endMs,
                      Long expireMs,
                      Long refreshMs) {
        this.periodIndex = periodIndex;
        this.startMs = startMs;
        this.endMs = endMs;
        this.expireMs = Optional.ofNullable(expireMs).orElse(endMs);
        Long realRefreshMs = Optional.ofNullable(refreshMs).orElse(-1L);
        long diffMs = endMs - startMs;
        this.refreshMs = realRefreshMs > diffMs ? -1 : realRefreshMs;
    }

    @Override
    public int getPeriodIndex() {
        return periodIndex;
    }

    @Override
    public void setPeriodIndex(int periodIndex) {
        if (!isHaveDad.compareAndSet(false, true)) {
            throw new RuntimeException("[Period233] already have other thread modify this periodIndex, it have dad");
        }
        this.periodIndex = periodIndex;
    }

    @Override
    public long getStartMs() {
        return startMs;
    }

    @Override
    public long getEndMs() {
        return endMs;
    }

    @Override
    public long getExpireMs() {
        return expireMs;
    }


    /**
     * 是否在周期内
     *
     * @param currentMs 当前毫秒
     * @return true 周期内
     */
    @Override
    public boolean isInPeriod(long currentMs) {
        return startMs <= currentMs && currentMs <= endMs;
    }


    /**
     * [Fixed] split by timeStep into a continuous Periods List <br>
     * 固定的时步间隔
     *
     * @param durationTime     一个周期持续多久
     * @param durationTimeUnit 持续单位
     * @param intervalTimeStep 周期 A,B 之间的间隔时间
     * @param intervalTimeUnit 间隔单位
     * @return 连续的小周期
     */
    @Override
    public PeriodDad233 toPeriodDad(long durationTime,
                                    TimeUnit durationTimeUnit,
                                    Integer intervalTimeStep,
                                    TimeUnit intervalTimeUnit) {
        List<Period233> period233List = new ArrayList<>();
        long durationMs = durationTimeUnit.toMillis(durationTime);
        long intervalMs = 0;
        if (intervalTimeStep != null && intervalTimeUnit != null) {
            intervalMs = intervalTimeUnit.toMillis(intervalTimeStep);
        }

        long tempStartMs = startMs;
        long tempEndMs = startMs + durationMs;

        long oneStepMs = durationMs + intervalMs;

        int periodCount = 1;
        while (tempStartMs < endMs) {
            Period233 of1 = Period233.of(periodCount, tempStartMs, tempEndMs);
            period233List.add(of1);

            tempStartMs += oneStepMs;
            tempEndMs += oneStepMs;

            periodCount++;
        }
        return PeriodDad233.from(period233List);
    }


    /**
     * 动态生成周期 by period count (小周期的次数, from 1 )
     *
     * @param durationMsFunc 单个周期持续多久
     * @param intervalMsFunc 周期与周期之间, 间隔多久
     * @return 周期 List
     */
    public PeriodDad233 generateDynamicPeriod(Function<Integer, Long> durationMsFunc,
                                              Function<Integer, Long> intervalMsFunc) {
        List<Period233> period233List = new ArrayList<>();


        long tempStartMs = startMs;
        long tempEndMs = tempStartMs;

        int periodCount = 1;
        while (tempStartMs < endMs) {
            // 根据次数, 动态生成常规间隔
            long periodDurationMs = durationMsFunc == null ? 0 : durationMsFunc.apply(periodCount);
            long intervalStepMs = intervalMsFunc == null ? 0 : intervalMsFunc.apply(periodCount);

            if (periodCount != 1) {
                tempStartMs += periodDurationMs;
            }
            tempEndMs += periodDurationMs;

            Period233 period233 = Period233.of(periodCount, tempStartMs, tempEndMs);
            period233List.add(period233);

            tempStartMs += intervalStepMs;
            tempEndMs += intervalStepMs;

            periodCount++;
        }
        return PeriodDad233.from(period233List);
    }

    @Override
    public long getRefreshMs() {
        return this.refreshMs;
    }

    @Override
    public List<Period233> getPeriodsList() {
        return Collections.singletonList(this);
    }

    public Period233 cloneToOther(int periodIndex) {
        return new Period233(
                periodIndex,
                this.startMs,
                this.endMs,
                this.expireMs,
                this.refreshMs
        );
    }
}
