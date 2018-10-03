/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:21
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.util;

import org.jleopard.util.DateUtil;
import org.jleopard.util.StringUtil;

import java.lang.reflect.Field;

public class ArgsTypeUtils {

    public static Object changeType(Field field, String str) {
        if (StringUtil.isEmpty(str)) {
            return null;
        }
        Object result = str;
        Class<?> type = field.getType();
        if (type == Long.class || type == long.class) {
            return Long.valueOf(str);
        }
        if (type == Integer.class || type == int.class) {
            return Integer.valueOf(str);
        }
        if (type == Byte.class || type == byte.class) {
            return Byte.valueOf(str);
        }
        if (type == Double.class || type == double.class) {
            return Double.valueOf(str);
        }
        if (type == Float.class || type == float.class) {
            return Float.valueOf(str);
        }
        if (type == java.util.Date.class) {
            return DateUtil.parseDatetime(str);
        }
        if (type == java.sql.Date.class) {
            return java.sql.Date.valueOf(str);
        }
        if (type == java.sql.Timestamp.class) {
            return java.sql.Timestamp.valueOf(str);
        }
        return result;
    }
}
