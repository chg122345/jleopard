package org.jleopard.bean;


import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.DateUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/12
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *  根据 字段的值类型来转化注入
 */
public class BeanInvoke {

    private static final Log LOG =LogFactory.getLog(BeanInvoke.class);
    public static void invokeField(Object bean, Field field, String value){
            field.setAccessible(true);
            Object fieldType=field.getType();
        try {
            if (fieldType==String.class){
                    field.set(bean,value);
            }else if (fieldType==int.class||fieldType==Integer.class ||fieldType==long.class){
                field.set(bean,Integer.parseInt(value));
            }else if (fieldType==Long.class){
                field.set(bean,Long.parseLong(value));
            }else if(fieldType==Double.class||fieldType==double.class){
                field.set(bean,Double.parseDouble(value));
            }else if(fieldType==Float.class||fieldType==float.class){
                field.set(bean,Float.parseFloat(value));
            }else if(fieldType==Boolean.class||fieldType==boolean.class){
                field.set(bean,Boolean.parseBoolean(value));
            }else if(fieldType== java.util.Date.class){
                field.set(bean,DateUtil.parseDate(value));
            }else if (fieldType==Time.class||fieldType==Timestamp.class||fieldType==Date.class){
                field.set(bean,value);
            }

        } catch (IllegalAccessException e) {
            LOG.error("invoke Field 失败..."+e);
            e.printStackTrace();
        }
    }
}
