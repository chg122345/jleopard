package com.leopardframework.util;

/**
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
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
    	
        return (array.length==0||array==null||"".equals(array[0]));
    }

    /**
     * 连接数组
     *//*
    public static Object[] concat(Object[] array1, Object[] array2) {
        return ArrayUtils.addAll(array1, array2);
    }

    *//**
     * 判断对象是否在数组中
     *//*
    public static <T> boolean contains(T[] array, T obj) {
        return ArrayUtils.contains(array, obj);
    }*/
}
