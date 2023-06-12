package com.neko233.datetime.settings;


import com.neko233.datetime.DateTime233;
import com.neko233.datetime.utils.KvTemplateForDt;

public class SystemClockSettings {

    /**
     * 修改系统时间 by timeZoneMs
     *
     * @param dateTime233 日期时间
     */
    public static void modifyOsDateTime(DateTime233 dateTime233) {
        modifyOsDateTime(dateTime233.zoneTimeMs());
    }

    /**
     * 修改系统时间
     *
     * @param trueTimeMs
     */
    public static void modifyOsDateTime(long trueTimeMs) {
        // 设置为你想要设置的时间
        String osName = System.getProperty("os.name").toLowerCase();
        try {
            Runtime runtime = Runtime.getRuntime();
            String template = "";
            if (osName.contains("win")) {
                // JDK
                template = "cmd /c date \"{yyyy-MM-dd HH:mm:ss}\"";
            } else if (osName.contains("linux")) {
                template = "date -s \"{yyyy-MM-dd HH:mm:ss}\"";
            } else if (osName.contains("mac")) {
                template = "date -s \"{yyyy-MM-dd HH:mm:ss}\"";
            } else {
                throw new IllegalArgumentException("not support this os = " + osName);
            }

            runtime.exec(KvTemplateForDt.builder(template)
                    .put("yyyy-MM-dd HH:mm:ss", DateTime233.of(trueTimeMs).toString())
                    .build());
        } catch (Exception e) {
            throw new RuntimeException("modify windows OS-system time error.", e);
        }

    }
}
