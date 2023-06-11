package com.neko233.datetime.utils;

import com.neko233.skilltree.commons.core.base.CollectionUtils233;

import com.neko233.skilltree.commons.core.base.StringUtils233;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextTokenUtils {

    @NotNull
    public static Map<String, String> matchFullyToTokenMap(String input,
                                                            String format,
                                                            String... tokens) {
        return matchFullyToTokenMap(input, format, Arrays.asList(tokens));
    }

    @NotNull
    public static Map<String, String> matchFullyToTokenMap(String input,
                                                            String format,
                                                            List<String> tokens) {

        // 不匹配空
        if (StringUtils.isBlank(input)
                || StringUtils233.isBlank(format)
                || CollectionUtils233.isEmpty(tokens)) {
            return Collections.emptyMap();
        }


        Map<Character, String> firstCharTokenMap = Optional.ofNullable(tokens).orElse(Collections.emptyList())
                .stream()
                .filter(StringUtils233::isNotBlank)
                .collect(Collectors.toMap(str -> str.charAt(0), Function.identity()));


        Map<String, String> tokenMap = new HashMap<>();

        char[] inputCharArray = input.toCharArray();
        char[] charArray = format.toCharArray();

        int totalLength = charArray.length;

        for (int i = 0; i < totalLength; i++) {
            char c = charArray[i];
            String targetStr = firstCharTokenMap.get(c);
            if (targetStr == null) {
                continue;
            }
            int strLength = targetStr.length();


            char[] formatChar = new char[strLength];
            for (int j = 0; j < strLength; j++) {
                if (i + j >= totalLength) {
                    break;
                }
                formatChar[j] = charArray[i + j];
            }

            // 临时匹配 token
            String tempTokenFormat = new String(formatChar);
            if (!tempTokenFormat.equals(targetStr)) {
                continue;
            }
            char[] inputChars = new char[strLength];
            for (int j = 0; j < strLength; j++) {
                if (i + j > totalLength) {
                    break;
                }
                inputChars[j] = inputCharArray[i + j];
            }
            String tempToken = new String(inputChars);
            tokenMap.put(targetStr, tempToken);
        }
        return tokenMap;
    }
}