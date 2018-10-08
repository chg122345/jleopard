package org.jleopard.util;

import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 对象实例化
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public final class ReflectionUtil {

	private static final Log LOGGER=LogFactory.getLog(ReflectionUtil.class);
	
	public static Object newInstance(Class<?> cls) {
		Object obj;
		try {
			obj=cls.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			LOGGER.error("实例化失败！",e);
			throw new RuntimeException(e); 
		}
		return obj;
	} 
	
	public static Object invokeMethod(Object obj,Method method,Object args) {
		Object mth;
		
		try {
			method.setAccessible(true);
			mth=method.invoke(obj, args);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LOGGER.error("调用方法失败",e);
			throw new RuntimeException(e); 
		}
		return mth;
	}
	
	public static void setField(Object obj,Field field,Object value) {
		
		try {
			field.setAccessible(true);
			field.set(obj, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			LOGGER.error("设值失败！",e);
			throw new RuntimeException(e); 
		}
		
	}
}
