package com.leopardframework.core.get;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class FieldValue {

    public static Object getFieldValue(Object obj,Field field){
        Object fieldValue;
        String fieldName=field.getName();
        String GetMethodName="get"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
        try {
            Method method=obj.getClass().getDeclaredMethod(GetMethodName);
            fieldValue=method.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fieldValue;
    }
}
