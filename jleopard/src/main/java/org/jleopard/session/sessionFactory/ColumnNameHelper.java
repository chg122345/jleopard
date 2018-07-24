package org.jleopard.session.sessionFactory;


import java.lang.reflect.Field;

import org.jleopard.core.annotation.Column;
import org.jleopard.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 *  获取字段名辅助类
 */
public class ColumnNameHelper {

    public static String getColumnName(Field field){
        Column column=field.getDeclaredAnnotation(Column.class);
        String columnName=column.value();
        if(StringUtil.isEmpty(columnName)){
            columnName=field.getName();
        }
        return columnName;
    }
}
