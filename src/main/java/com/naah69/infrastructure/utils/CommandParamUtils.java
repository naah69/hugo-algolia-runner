package com.naah69.infrastructure.utils;

import java.util.Objects;

public class CommandParamUtils {

    public static boolean optionalEnbale(boolean[] enable) {
        boolean result = false;
        if (Objects.nonNull(enable)) {
            for (boolean r : enable) {
                result = result || r;
                if (result) {
                    return result;
                }
            }
        }
        return result;
    }
}
