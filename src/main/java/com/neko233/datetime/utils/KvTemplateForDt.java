package com.neko233.datetime.utils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Key-Value 模板. placeholder = ${key}
 *
 * @author SolarisNeko on 2021-07-01
 **/
public class KvTemplateForDt {

    private static final Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");


    private final String kvTemplate;
    private final Map<String, Object> kvMap = new HashMap<>(2, 0.8f);

    public KvTemplateForDt(String kvTemplate) {
        this.kvTemplate = kvTemplate;
    }

    public static KvTemplateForDt builder(String kvTemplate) {
        if (StringUtilsForDt.isBlank(kvTemplate)) {
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

    public KvTemplateForDt put(Map<String, ? extends Object> kv) {
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


    /**
     * }
     * 使用了正则表达式 \\$\\{([^}]+)\\} 来匹配 ${name} 样式的占位符
     * <p>
     * \\$ 表示匹配 $ 字符
     * \{ 和 \} 表示匹配 { 和 } 字符
     * [^}]+ 表示匹配任意不是 } 的字符，这里使用了非贪婪模式，即 + 后面加上 ?，表示匹配尽可能少的字符，避免出现匹配多个占位符的情况。
     * <br>
     * Hello, ${name}! Welcome to ${city}. -> name, city
     *
     * @param template 输入文本
     * @return 占位符
     */
    public static List<String> parsePlaceHolder(String template) {
        List<String> placeholders = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            placeholders.add(placeholder);
        }

        return placeholders;
    }

    public static List<String> parsePlaceHolderAndIndex(String template) {
        List<String> placeholders = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(template);

        while (matcher.find()) {
            String placeholder = matcher.group(1);
            placeholders.add(placeholder);
        }

        return placeholders;
    }

    public static List<Integer> getPlaceholderIndexes(String text, String placeholder) {
        List<Integer> indexes = new ArrayList<>();

        // 构建正则表达式
        String regex = "\\$\\{" + Pattern.quote(placeholder) + "\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);

        // 查找并记录所有占位符的位置
        while (matcher.find()) {
            indexes.add(matcher.start());
        }

        return indexes;
    }

    public static Map<String, List<Integer>> find(String template) {
        Map<String, List<Integer>> result = new HashMap<>();

        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            String placeholder = matcher.group(1);
            int index = matcher.start(0);

            List<Integer> positions = result.getOrDefault(placeholder, new ArrayList<>());
            positions.add(index);
            result.put(placeholder, positions);
        }

        return result;
    }
}
