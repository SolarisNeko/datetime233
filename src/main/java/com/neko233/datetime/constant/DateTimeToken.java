package com.neko233.datetime.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface DateTimeToken {

    // 年
    String YEAR = "yyyy";
    // 月
    String MONTH = "MM";
    // 日
    String DAY = "dd";
    // 分
    String HOUR = "HH";
    // 时
    String MINUTE = "mm";
    // 秒
    String SECONDS = "ss";
    // 毫秒
    String MILLISECONDS = "SSS";

    /**
     * 所有 token
     */
    List<String> DATE_TIME_TOKEN_LIST = Collections.unmodifiableList(Arrays.asList(

            YEAR,

            MONTH,

            DAY,

            HOUR,

            MINUTE,

            SECONDS,

            MILLISECONDS
    ));

}
