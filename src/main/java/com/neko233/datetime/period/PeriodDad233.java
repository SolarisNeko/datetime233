package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;
import com.neko233.skilltree.commons.core.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 周期的爸, 持有一系列有序的 Period[] order by time (相当于孩子) <br>
 * 会修改 Period 的状态 periodIndex (因为 Period 是 PeriodDad 的一个子结构, 只隶属于其中一个 PeriodDad )
 * 并带有 periodIndex from 1 to n+
 *
 * @author SolarisNeko on 2023-04-24
 **/
@EqualsAndHashCode
@ToString
public class PeriodDad233 {

    // 开始
    private final long allStartMs;
    // 结束
    private final long allEndMs;
    // 刷新时间
    private final long allExpireMs;
    private final int minPeriodIndex;
    private final int maxPeriodIndex;
    private final Map<Integer, Period233> periodIndexToPeriodMap = new TreeMap<>();

    private PeriodDad233(List<Period233> periodsList,
                         long allExpireMs) {
        AtomicInteger periodCount = new AtomicInteger(1);
        // 重新标定周期顺序
        List<Period233> periodsSortList = Optional.ofNullable(periodsList).orElse(Collections.emptyList())
                .stream()
                .sorted(Comparator.comparingLong(Period233::getStartMs))
                .map(p -> p.cloneToOther(periodCount.getAndIncrement()))
                .collect(Collectors.toList());

        this.minPeriodIndex = 1;
        int maxPeriodIndex = 1;
        for (Period233 period233 : periodsSortList) {
            int periodIndex = period233.getPeriodIndex();
            periodIndexToPeriodMap.put(periodIndex, period233);
            if (periodIndex > maxPeriodIndex) {
                maxPeriodIndex = periodIndex;
            }
        }
        this.maxPeriodIndex = maxPeriodIndex;
        this.allExpireMs = allExpireMs;

        this.allStartMs = periodsSortList.stream().map(Period233::getStartMs).min(Long::compare).orElse(0L);
        this.allEndMs = periodsSortList.stream().map(Period233::getEndMs).max(Long::compare).orElse(0L);
    }

    public static PeriodDad233 from(Period233 period, int oncePeriodDurationMs) {
        return period.toPeriodDad(oncePeriodDurationMs, TimeUnit.MILLISECONDS);
    }

    public static PeriodDad233 from(List<Period233> periodsList) {
        return new PeriodDad233(periodsList, -1);
    }

    public static PeriodDad233 from(List<Period233> periodsList,
                                    long refreshMsInPeriod) {
        return new PeriodDad233(periodsList, refreshMsInPeriod);
    }

    /**
     * @param consumer 消费每一个周期
     */
    public void forEach(Consumer<Period233> consumer) {
        for (Period233 periods : periodIndexToPeriodMap.values()) {
            consumer.accept(periods);
        }
    }


    public List<Period233> getPeriodsSortList() {
        return new ArrayList<>(periodIndexToPeriodMap.values());
    }

    public long getAllStartMs() {
        return this.allStartMs;
    }

    public long getAllEndMs() {
        return this.allEndMs;
    }

    public long getAllExpireMs() {
        return allExpireMs;
    }

    public int getChildPeriodCount() {
        return getPeriodsSortList().size();
    }


    public Period233 getCurrentPeriod() {
        return getCurrentPeriod(System.currentTimeMillis());
    }

    public Period233 getCurrentPeriod(DateTime233 dateTime) {
        return getCurrentPeriod(dateTime.originalTimeMs());
    }

    /**
     * 当都不在周期内, 返回空
     *
     * @param currentMs 当前毫秒
     * @return 所在周期
     */
    @Nullable
    public Period233 getCurrentPeriod(long currentMs) {
        for (Period233 period233 : this.getPeriodsSortList()) {
            boolean inPeriod = period233.isInPeriod(currentMs);
            // 不在周期中
            if (!inPeriod) {
                continue;
            }
            // 某个赛季周期中
            return period233;
        }
        return null;
    }

    public int getMaxPeriodIndex() {
        return this.maxPeriodIndex;
    }

    public int getMinPeriodIndex() {
        return this.minPeriodIndex;
    }
}
