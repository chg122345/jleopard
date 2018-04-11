package com.leopardframework.core.get;

import com.leopardframework.core.annotation.Table;
import com.leopardframework.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class TableName {
    public static String getTableName(Class<?> cls){
        Table table=cls.getDeclaredAnnotation(Table.class);
        String tablename=table.value().toUpperCase();
        if(StringUtil.isEmpty(tablename)){
            tablename=cls.getSimpleName().toUpperCase();
        }
        return tablename;
    }
}
