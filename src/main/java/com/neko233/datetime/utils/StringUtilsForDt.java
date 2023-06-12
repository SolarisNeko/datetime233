package com.neko233.datetime.utils;


public class StringUtilsForDt {


    /**
     * 安全 trim
     *
     * @param input 输入
     * @return 内容
     */
    public static String trim(final String input) {
        if (StringUtilsForDt.isBlank(input)) {
            return "";
        }
        return input.trim();
    }

    public static boolean isEmpty(final char... cs) {
        return isEmpty(new String(cs));
    }

    /**
     * 空字符串判断
     */
    public static boolean isEmpty(final CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(final CharSequence cs) {
        return !isEmpty(cs);
    }

    public static boolean isBlank(final CharSequence cs) {
        final int strLen = length(cs);
        if (strLen == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * <p>Checks if a CharSequence is not empty (""), not null and not whitespace only.</p>
     *
     * <p>Whitespace is defined by {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils233.isNotBlank(null)      = false
     * StringUtils233.isNotBlank("")        = false
     * StringUtils233.isNotBlank(" ")       = false
     * StringUtils233.isNotBlank("bob")     = true
     * StringUtils233.isNotBlank("  bob  ") = true
     * </pre>
     *
     * @param cs the CharSequence to check, may be null
     * @since 0.1.8
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }


    public static int length(final CharSequence cs) {
        return cs == null ? 0 : cs.length();
    }

}
