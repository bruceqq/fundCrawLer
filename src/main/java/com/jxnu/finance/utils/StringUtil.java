package com.jxnu.finance.utils;

public class StringUtil {

    /**
     * 是否默认值
     *
     * @param keyword
     * @return
     */
    public static Boolean isBank(String keyword) {
        if (keyword == null || keyword.length() == 0
            || keyword.trim().length() == 0
            || "-".equals(keyword)) {
            return true;
        }
        return false;
    }
}
