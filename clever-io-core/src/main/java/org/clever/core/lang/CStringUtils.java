package org.clever.core.lang;

import java.util.Objects;

/**
 * 说明: 字符串的工具
 */
public class CStringUtils {
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

   public static boolean equals(String str1, String str2) {
        return Objects.equals(str1, str2);
    }


}
