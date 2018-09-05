package org.jleopard.session.sessionFactory;

import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.DateUtil;

import java.lang.reflect.Field;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-09-05  下午2:12
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
final class InvokeTypeHelper {

    private static final Log log = LogFactory.getLog(InvokeTypeHelper.class);

    public static Object changeType(Class<?> cls, String fieldName, Object value) {
        String str=String.valueOf(value);
        Object result = str;
        try {
            Field field = cls.getDeclaredField(fieldName);
            Class<?> type = field.getType();
            if (type == Long.class || type == long.class){
                result = Long.valueOf(str);
            }else if (type == Integer.class || type == int.class){
                result = Integer.valueOf(str);
            }else if (type == Byte.class || type == byte.class){
                result = Byte.valueOf(str);
            }else if (type == Double.class || type == double.class){
                result = Double.valueOf(str);
            }else if (type == Float.class || type == float.class){
                result = Float.valueOf(str);
            }else if (type == java.util.Date.class){
                result = DateUtil.parseDatetime(str);
            }else if (type == java.sql.Date.class){
                result = java.sql.Date.valueOf(str);
            }else if (type == java.sql.Timestamp.class){
                result = java.sql.Timestamp.valueOf(str);
            }
        } catch (NoSuchFieldException e) {
            log.error("类型转化出错..",e);
           return null;
        }
        return result;
    }
}
