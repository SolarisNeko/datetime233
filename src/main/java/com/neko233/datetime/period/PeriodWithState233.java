package com.neko233.datetime.period;

import lombok.Data;

/**
 * @author SolarisNeko on 2023-04-24
 **/
@Data
public class PeriodWithState233 {

    // [State] 当前时间
    private long currentMs;
    // [State] 是否在周期中
    private boolean isInPeriod;
    // [State] 已刷新了多少次
    private long refreshCount;

    // 开始时间
    private long startMs;
    // 结束时间
    private long endMs;
    // 周期过期时间
    private long expireMs;
    // 周期内多久刷新一次时间, < 0 == 不刷新
    private long refreshMs;

    public PeriodWithState233(long startMs,
                              long endMs,
                              long expireMs,
                              long refreshMs) {
        this.isInPeriod = false;
        this.startMs = startMs;
        this.endMs = endMs;
        this.expireMs = expireMs;
        this.refreshMs = refreshMs;
    }

    /**
     * 不带状态的 create
     *
     * @param startMs   开始
     * @param endMs     结束
     * @param expireMs  过期
     * @param refreshMs 刷新
     * @return 周期带状态
     */
    public static PeriodWithState233 of(long startMs,
                                        long endMs,
                                        long expireMs,
                                        long refreshMs) {
        return new PeriodWithState233(startMs, endMs, expireMs, refreshMs);
    }

    public long getPeriodCount() {
        return this.refreshCount + 1;
    }

    private PeriodWithState233(boolean isInPeriod) {
        this.isInPeriod = isInPeriod;
    }

    public static PeriodWithState233 error() {
        return new PeriodWithState233(false);
    }

    public boolean isInPeriod() {
        return isInPeriod(this.currentMs);
    }

    public boolean isInPeriod(long currentMs) {
        return startMs <= currentMs && currentMs <= endMs;
    }

    public boolean getIsInPeriod() {
        return isInPeriod;
    }

    /**
     * 基于输入的时间进行刷新
     *
     * @param currentMs input your current timeMs
     */
    public PeriodWithState233 refresh(long currentMs) {
        if (isNotInPeriod(currentMs)) {
            this.isInPeriod = false;
        }
        this.isInPeriod = true;
        long tempStartMs = startMs;
        long refreshCount = 0;
        while (tempStartMs <= currentMs) {
            if (this.refreshMs < 0) {
                break;
            }
            tempStartMs += this.refreshMs;

            refreshCount += 1;
        }

        this.refreshCount = refreshCount;
        return this;
    }

    private boolean isNotInPeriod(long currentMs) {
        return !isInPeriod(currentMs);
    }

    public PeriodWithState233 refreshNow() {
        return refresh(System.currentTimeMillis());
    }


}
