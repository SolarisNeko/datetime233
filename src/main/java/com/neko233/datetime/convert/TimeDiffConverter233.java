package com.neko233.datetime.convert;


import com.neko233.skilltree.commons.core.base.StringUtils233;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 时间转换器. 一般用于计算[ 活动、支付、等 ] 场景的倒计时之类的, 以及业务中低代码的基础 <br>
 * 一般用法:
 * <code>
 * // switch each
 * 8640600000L ms <--> 1 second 200 ms
 * </code>
 *
 * @author SolarisNeko
 * Date on 2023-06-11
 */
public class TimeDiffConverter233 {

    // order by ms DESC , 维度越高越重要
    public static final Map<String, Long> CONVERSION_TIME_TEXT_MAP = Collections.unmodifiableMap(new LinkedHashMap<String, Long>() {{
        put("day", 86400000L);
        put("hour", 3600000L);
        put("minute", 60000L);
        put("second", 1000L);
        put("ms", 1L);
    }});


    /**
     * 尽可能用更高维度的时间单位, 表示数据
     *
     * @param milliseconds 毫秒
     * @return 字符串 "${number} ${unit}"
     */
    public static String convertMsToText(long milliseconds) {

        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Long> entry : CONVERSION_TIME_TEXT_MAP.entrySet()) {
            String unit = entry.getKey();
            long conversion = entry.getValue();

            if (milliseconds >= conversion) {
                long value = milliseconds / conversion;
                milliseconds %= conversion;

                if (result.length() > 0) {
                    result.append(" ");
                }
                result.append(value).append(" ").append(unit);
            }
        }

        return result.toString();
    }

    /**
     * 将一个文本, 转为
     *
     * @param text 文本
     * @return 转换后的时间戳
     */
    public static long convertTextToMs(String text) {
        if (text == null) {
            return 0L;
        }
        List<String> tokenList = Arrays.stream(text.split(" "))
                .filter(StringUtils233::isNotBlank)
                .map(String::trim)
                .collect(Collectors.toList());

        long totalMs = 0;

        for (int i = 0; i < tokenList.size(); i = i + 2) {
            String valueText = tokenList.get(i);
            String unitText = tokenList.get(i + 1);

            if (StringUtils233.isBlank(valueText)) {
                throw new IllegalArgumentException();
            }

            int value = Integer.parseInt(valueText);

            if (CONVERSION_TIME_TEXT_MAP.containsKey(unitText)) {
                totalMs += value * CONVERSION_TIME_TEXT_MAP.get(unitText);
            }
        }

        return totalMs;
    }
}

