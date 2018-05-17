package com.leopardframework.core.sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/19
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class JoinSql implements Sql {

    private static final Log LOG=LogFactory.getLog(JoinSql.class);

    private String tableName1;  //表名1

    private String tableName2;  //表名2

    private String foreignKey; //外键

    private String t2PrimaryKey;

    private List<String> allColumnNames; //所有字段名

    private List<String> t2ColumnNames; //所有字段名


    public JoinSql(Class<?> cls1,Class<?> cls2) {
        List<String> allColumns=new ArrayList<>();
        List<String> t2Columns=new ArrayList<>();
        this.tableName1 =TableUtil.getTableName(cls1);
        this.tableName2=TableUtil.getTableName(cls2);
        this.foreignKey=FieldUtil.getForeignKeyName(cls1).get(0);
        this.t2PrimaryKey=FieldUtil.getPrimaryKeys(cls2).get(0);
        Set<String> set=FieldUtil.getAllColumnName(cls1);
        allColumns.addAll(set);
        Set<String> set2= FieldUtil.getAllColumnName(cls2);
        t2Columns.addAll(set2);
        /*for (String column:set2){
            if(column.equals(t2PrimaryKey)){
               continue;
            }else{
                t2Columns.add(column);
            }
        }*/
        this.allColumnNames=allColumns;
        this.t2ColumnNames=t2Columns;
    }

    @Override
    public List<String> getColumnNames() {
        return allColumnNames;
    }

    @Override
    public List<Object> getValues() {
        return null;
    }

    @Override
    public String getSql() {

       StringBuilder ON=new StringBuilder();
        ON.append("\n").append(" on").append(" ").append(tableName1).append(".").append(foreignKey)
               .append("=").append(tableName2).append(".").append(t2PrimaryKey).append(" ");

        StringBuilder COL=new StringBuilder();
        for (int i=0,j=allColumnNames.size();i<j;++i){
            if (i>0){
                COL.append(",");
            }
            COL.append(tableName1).append(".").append(allColumnNames.get(i));
        }
        for (int i=0,j=t2ColumnNames.size();i<j;++i){

            COL.append(",").append(tableName2).append(".").append(t2ColumnNames.get(i));
        }
        StringBuilder SQL=new StringBuilder();
        String sql=String.format("select %s from "+tableName1+" left join "+tableName2,COL.toString());
        SQL.append(sql).append(ON);
        LOG.info("生成的sql语句："+SQL.toString());
        return SQL.toString();
    }
}
