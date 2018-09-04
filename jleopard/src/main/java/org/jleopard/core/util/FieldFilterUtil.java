package org.jleopard.core.util;
/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Sep 4, 2018 7:09:31 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 * 过滤特定的字段
 */

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Map;

public final class FieldFilterUtil {
	
	/**
	 * 
	 * @param columnField 包含所有的字段
	 * @param map 所有字段和值
	 * @return  过滤后的字段和值
	 */
	public static  Map<String,Object> filterListFeild( Map<String, Field> columnField, Map<String,Object> map){
		 columnField.forEach((c, f) -> {
				if (Collection.class.isAssignableFrom(f.getType())) {
					ParameterizedType pm = (ParameterizedType) f.getGenericType();
					Class<?> fcls = (Class<?>) pm.getActualTypeArguments()[0];
					if (TableUtil.isTable(fcls)) {
						map.remove(c);
					}
				}
			});
		 return map;
	}

}
