package com.leopardframework.core.sql;

import com.leopardframework.core.annotation.Column;
import com.leopardframework.core.session.sessionFactory.ColumnNameHelper;
import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.JavaTypeUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.exception.SqlBuilderException;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.util.StringUtil;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/11
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class CreateTableSql implements Sql{

    private static final Log LOG=LogFactory.getLog(CreateTableSql.class);

    private Class<?> cls;

    private String tableName;  //表名

    public CreateTableSql(Class<?> cls) {
        this.cls=cls;
        this.tableName =TableUtil.getTableName(cls);
        if(StringUtil.isEmpty(tableName)){
            LOG.error(cls+" 无 @Table注解");
            throw new SqlBuilderException(cls+" 无@Table注解...");
        }

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
     *   自动建表sql语句
     *
     * @return
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("create table if not exists ").append(tableName).append("\n").append(" (");
        Field[] fields=cls.getDeclaredFields();
        for (Field field:fields){
          Column column=field.getDeclaredAnnotation(Column.class);
          if (FieldUtil.isPrimaryKey(column)==0){
              if(column.allowNull()){
                  SQL.append(ColumnNameHelper.getColumnName(field)).append(" ").append(JavaTypeUtil.getSqlType(field.getType()))
                          .append(",").append("\n");
              }else{
                  SQL.append(ColumnNameHelper.getColumnName(field)).append(" ").append(JavaTypeUtil.getSqlType(field.getType()))
                      .append(" ").append("not null").append(",").append("\n");
              }
          }else if (FieldUtil.isPrimaryKey(column)==1){
              SQL.append(ColumnNameHelper.getColumnName(field)).append(" ").append(JavaTypeUtil.getSqlType(field.getType()))
                      .append(" ").append("primary key").append(" ").append("not null").append(",").append("\n");
          }else if (FieldUtil.isPrimaryKey(column)==2){
              SQL.append(ColumnNameHelper.getColumnName(field)).append(" ").append(JavaTypeUtil.getSqlType(field.getType()))
                      .append(" ").append("primary key").append(" ") .append("auto_increment")
                      .append(" ").append("not null").append(",").append("\n");
          }

        }
        SQL.deleteCharAt(SQL.length()-2).append(")");

        return SQL.toString().toUpperCase();
    }
}
