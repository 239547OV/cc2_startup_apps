package com.aa.crewcomp.orionjdbc.utils;

import java.lang.reflect.Field;

public class ObjectSizeEstimator {
    public static <T> int estimateSize(Class<T> clazz) {
        int totalSize = 0;
        for (Field field : clazz.getDeclaredFields()) {
            Class<?> type = field.getType();
            totalSize += getFieldSize(type);
        }
        return totalSize;
    }

    private static int getFieldSize(Class<?> type) {
        if (type == boolean.class || type == byte.class) return 1;
        if (type == char.class || type == short.class) return 2;
        if (type == int.class || type == float.class) return 4;
        if (type == long.class || type == double.class) return 8;
        return 4; // Approximate reference size
    }
}