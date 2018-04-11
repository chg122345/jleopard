package com.leopardframework.core.get;

import com.leopardframework.core.annotation.Column;
import com.leopardframework.core.enums.Primary;
import com.leopardframework.exception.NotfoundFieldException;
import com.leopardframework.util.ArrayUtil;

import java.lang.reflect.Field;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class PrimaryKeyName {

    public static String getPrimaryKeyName(Class<?> cls){
        String primaryKeyName=null;
        Field[] fields=cls.getDeclaredFields();
        if(ArrayUtil.isEmpty(fields)){
            throw new NotfoundFieldException(cls+" 没有成员变量...");
        }
        for(Field field:fields) {
            boolean fexist = field.isAnnotationPresent(Column.class);
            if (!fexist) {
                continue;
            }
            Column column=field.getDeclaredAnnotation(Column.class);
            if(column.isPrimary()==Primary.AUTOINCREMENT||column.isPrimary()==Primary.YSE){
                primaryKeyName=ColumnName.getColumnName(field).toUpperCase();
            }
        }
        return primaryKeyName;
    }
}
