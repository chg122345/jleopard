package org.jleopard.util;

import java.util.Collection;
import java.util.Map;


/**
 * 
 * 集合判断工具
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class CollectionUtil {

    /**
     * 判断集合是否非空
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判断集合是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        return (collection==null||collection.isEmpty()||collection.size()==0);
    }
    
    /**
	 * 判断map集合是否为空
	 * @param map
	 * @return
	 */
	public static boolean isEmpty(Map<?,?> map) {
		return (map==null||map.isEmpty());
	}
	public static boolean isNotEmpty(Map<?,?> map) {
		return !isEmpty(map);
	}
}
