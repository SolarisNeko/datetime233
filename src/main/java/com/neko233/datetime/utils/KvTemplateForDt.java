package com.neko233.datetime.utils;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Key-Value 模板. placeholder = ${key}
 *
 * @author SolarisNeko on 2021-07-01
 **/
public class KvTemplateForDt {

    private final String kvTemplate;
    private final Map<String, Object> kvMap = new HashMap<>(2, 0.8f);

    public KvTemplateForDt(String kvTemplate) {
        this.kvTemplate = kvTemplate;
    }

    public static KvTemplateForDt builder(String kvTemplate) {
        if (StringUtils.isBlank(kvTemplate)) {
            throw new RuntimeException("your kv template is blank !");
        }
        return new KvTemplateForDt(kvTemplate);
    }


    public KvTemplateForDt mergeJoin(String key, Object value, String union) {
        kvMap.merge(key, value, (v1, v2) -> v1 + union + v2);
        return this;
    }

    public KvTemplateForDt put(String key, Object value) {
        kvMap.put(key, value);
        return this;
    }

    public KvTemplateForDt put(Map<String, Object> kv) {
        if (kv == null) {
            return this;
        }
        kv.forEach(this::put);
        return this;
    }

    public String build() {
        return this.toString();
    }

    @Override
    public String toString() {
        // 替换全部
        StringBuilder sb = new StringBuilder(kvTemplate);
        for (Map.Entry<String, Object> entry : kvMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            String placeholder = "\\$\\{" + key + "\\}";
            Matcher matcher = Pattern.compile(placeholder).matcher(sb);
            int startIndex = 0;
            while (matcher.find(startIndex)) {
                sb.replace(matcher.start(), matcher.end(), String.valueOf(value));
                startIndex = matcher.start() + String.valueOf(value).length();
            }
        }
        return sb.toString();
    }

    @NotNull
    public static String generateTemplate(String toFormatText,
                                          Map<String, ?> kv) {
        Set<String> keySet = Optional.ofNullable(kv).orElse(Collections.emptyMap()).keySet();
        return generateTemplate(toFormatText, keySet);
    }

    @NotNull
    public static String generateTemplate(String toFormatText, Set<String> keySet) {
        String newFormat = Optional.ofNullable(toFormatText).orElse("");
        for (String key : Optional.ofNullable(keySet).orElse(Collections.emptySet())) {
            newFormat = newFormat.replaceAll(key, "\\${" + key + "}");
        }
        return newFormat;
    }
}