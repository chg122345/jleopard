package com.leopardframework.core.sql;

import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class SelectSqlMore implements Sql{

    private static final Log log=LogFactory.getLog(SelectSqlMore.class);

    private String tableName;  //表名

    private List<String> allColumnNames; //所有字段名

    private Object primaryKeyName;

    public SelectSqlMore(Class<?> cls) {
        List<String> allcolumns=new ArrayList<>();
        this.tableName =TableUtil.getTableName(cls);
        this.primaryKeyName=FieldUtil.getPrimaryKeys(cls).get(0);
        Set<String> set=FieldUtil.getAllColumnName(cls);
        for(String column:set){
            allcolumns.add(column);
        }
        this.allColumnNames=allcolumns;

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
     *    SELECT ID,NAME,PHONE,ADDRESS FROM USER
     *
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("select ");
        for(String columnname:allColumnNames){
            SQL.append(columnname).append(",");
        }
        SQL.deleteCharAt(SQL.length()-1).append(" ").append("from").append(" ").append(tableName)
                .append("\n")/*.append("   where").append(" ").append(primaryKeyName)*/;
        log.info(" 生成的sql语句: "+SQL.toString().toUpperCase());
        return SQL.toString().toUpperCase();
    }
}
