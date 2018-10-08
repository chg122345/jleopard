package org.jleopard.util;

/**
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *
 * 判断数组是否为空
 *
 */
public class ArrayUtil {

    /**
     * 判断数组是否非空
     */
    public static boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }

    /**
     * 判断数组是否为空
     */
    public static boolean isEmpty(Object[] array) {
    	
        return (null==array || 0==array.length);
    }

}
