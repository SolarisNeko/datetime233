package com.neko233.datetime.period;

import com.neko233.datetime.api.PeriodApi;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * used to hold a series of Period233 <br>
 * 周期 老爸, 用于表示持有一系列 Period233 children <br>
 *
 * @author SolarisNeko on 2023-04-24
 **/
@EqualsAndHashCode
@ToString
public class PeriodDaddy233 implements PeriodApi {

    private final long startMs;
    private final long endMs;
    private List<Period233> periodsList = new ArrayList<>();
    // 在周期内刷新的时间, 固定 |
    private final long refreshMsInPeriod;

    private PeriodDaddy233(List<Period233> periodsList, long refreshMs) {
        this.periodsList = Optional.ofNullable(periodsList).orElse(Collections.emptyList());
        this.refreshMsInPeriod = refreshMs;

        this.startMs = this.periodsList.stream().map(Period233::getStartMs).min(Long::compare).orElse(0L);
        this.endMs = this.periodsList.stream().map(Period233::getEndMs).max(Long::compare).orElse(0L);
    }

    public static PeriodDaddy233 from(List<Period233> periodsList) {
        return new PeriodDaddy233(periodsList, -1);
    }

    public static PeriodDaddy233 from(List<Period233> periodsList, long refreshMsInPeriod) {
        return new PeriodDaddy233(periodsList, refreshMsInPeriod);
    }


    public void forEach(Consumer<Period233> consumer) {
        for (Period233 periods : periodsList) {
            consumer.accept(periods);
        }
    }

    @Override
    public PeriodDaddy233 toPeriodsDaddy() {
        return this;
    }

    @Override
    public List<Period233> getPeriodsList() {
        return Optional.ofNullable(periodsList).orElse(Collections.emptyList());
    }

    @Override
    public long getStartMs() {
        return this.startMs;
    }

    @Override
    public long getEndMs() {
        return this.endMs;
    }

    public long getRefreshMs() {
        return refreshMsInPeriod;
    }

    public int getChildPeriodCount() {
        return getPeriodsList().size();
    }
}
