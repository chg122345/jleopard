package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.annotation.Column;
import com.leopardframework.util.StringUtil;

import java.lang.reflect.Field;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *  获取字段名辅助类
 */
public class ColumnNameHelper {

    public static String getColumnName(Field field){
        Column column=field.getDeclaredAnnotation(Column.class);
        String columnName=column.value()/*.toUpperCase()*/;
        if(StringUtil.isEmpty(columnName)){
            columnName=field.getName()/*.toUpperCase()*/;
        }
        return columnName;
    }
}
