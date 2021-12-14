package com.naah69.infrastructure.utils;

public class StringUtils {

    public static boolean isBlank(String dir) {
        return dir == null || dir.isBlank();
    }
    public static boolean isNotBlank(String dir) {
        return !isBlank(dir);
    }
}
