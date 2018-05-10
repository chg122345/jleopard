package com.leopardframework.core.sql;

import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public interface Sql {
    /**
     *    字段名
     */
     List<String> getColumnNames();

    /**
     *   字段名对应的 value
     */
     List<Object> getValues();

    /**
     *  获取拼接好的sql
     */
     String getSql();
}
