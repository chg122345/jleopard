package com.leopardframework.core.sql;

import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;

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
public class DeleteSqlMore implements Sql {

    private static final Log log=LogFactory.getLog(DeleteSqlMore.class);

    private String tableName;

    private Object primaryKeyName;

    public DeleteSqlMore(Class<?> cls){
        this.tableName =TableUtil.getTableName(cls);
        this.primaryKeyName=FieldUtil.getPrimaryKeys(cls).get(0);
    }
    @Override
    public List<String> getColumnNames() {
        return null;
    }

    @Override
    public List<Object> getValues() {
        return null;
    }

    /**
     *
     * @return
     *   DELETE FROM USER
     *     WHERE ID
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("delete from ").append(tableName).append("\n").append(" ").append("where ").append(primaryKeyName);
        log.info(" 生成的sql语句: "+SQL.toString().toUpperCase());
        return SQL.toString().toUpperCase();
    }
}
