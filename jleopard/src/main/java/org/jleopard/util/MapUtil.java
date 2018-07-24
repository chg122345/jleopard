package org.jleopard.util;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Map判断非空
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class MapUtil {

    /**
     * 判断 Map 是否非空
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * 判断 Map 是否为空
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return (map==null||map.isEmpty());
    }

    /**
     * 转置 Map
     */
    public static <K, V> Map<V, K> invert(Map<K, V> source) {
        Map<V, K> target = null;
        if (isNotEmpty(source)) {
            target = new LinkedHashMap<V, K>(source.size());
            for (Map.Entry<K, V> entry : source.entrySet()) {
                target.put(entry.getValue(), entry.getKey());
            }
        }
        return target;
    }
}
