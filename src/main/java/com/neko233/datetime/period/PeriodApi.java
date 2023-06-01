package com.neko233.datetime.period;

import com.neko233.datetime.DateTime233;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author SolarisNeko
 * Date on 2023-04-26
 */
public interface PeriodApi {

    /**
     * 第几个周期
     *
     * @return 当只有一个周期时, default 1. 当为 0 时, 需要外部自己判断
     */
    int getPeriodIndex();

    void setPeriodIndex(int periodIndex) throws IllegalAccessException;

    /**
     * @return 周期开始的时间 timeMs
     */
    long getStartMs();

    /**
     * @return 周期结束的时间 timeMs
     */
    long getEndMs();

    /**
     * @return 刷新毫秒, 在单个 Period 中, 从 startMs 开始固定生成一系列的 refreshMs
     */
    long getRefreshMs();

    default long getPeriodDurationMs() {
        return getEndMs() - getStartMs();
    }

    /**
     * @return 返回持有多个周期的 PeriodDaddy233
     */
    default PeriodDad233 toPeriodDad() {
        long periodDurationMs = getPeriodDurationMs();
        return toPeriodDad(periodDurationMs, TimeUnit.MILLISECONDS);
    }

    default PeriodDad233 toPeriodDad(long duration,
                                     TimeUnit durationTimeUnit) {
        return toPeriodDad(duration, durationTimeUnit, null, null);
    }

    PeriodDad233 toPeriodDad(long durationTime,
                             TimeUnit durationTimeUnit,
                             Integer intervalTimeStep,
                             TimeUnit intervalTimeUnit);

    /**
     * @return 获取周期列表
     */
    List<Period233> getPeriodsList();

    default int getChildPeriodCount() {
        return 0;
    }

    long getExpireMs();

    /**
     * @return 是否需要刷新
     */
    default boolean isNeedRefresh() {
        if (getRefreshMs() > 0) {
            return true;
        }
        return false;
    }

    /**
     * @return 是否不需要刷新
     */
    default boolean isNotNeedRefresh() {
        return !isNeedRefresh();
    }

    /**
     * 获取所有 周六/周日
     *
     * @return 周六日 Date 集合
     */
    default List<DateTime233> getAllWeekends() {
        List<DateTime233> dateTime233List = new ArrayList<>();
        long tempMs = getStartMs();
        while (tempMs < getEndMs()) {
            DateTime233 of = DateTime233.ofZeroClock(tempMs);
            int weekday = of.getWeekDay();
            if (weekday == 6) {
                dateTime233List.add(of);
                tempMs += TimeUnit.DAYS.toMillis(1);
                continue;
            }
            if (weekday == 7) {
                dateTime233List.add(of);
                tempMs += TimeUnit.DAYS.toMillis(6);
                continue;
            }
            tempMs += TimeUnit.DAYS.toMillis(1);
        }
        return dateTime233List;
    }

    boolean isInPeriod(long currentMs);

    default boolean isNotInPeriod(long currentMs) {
        return !isInPeriod(currentMs);
    }


}
