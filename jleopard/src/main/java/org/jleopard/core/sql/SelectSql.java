package org.jleopard.core.sql;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class SelectSql implements Sql, CloumnNames, CloumnValue{

    private static final Log log=LogFactory.getLog(SelectSql.class);

    private String tableName;  //表名

    private List<String> columnNames; //字段名

    private List<String> allColumnNames; //所有字段名

    private List<Object> values;  //字段对应的值

    public SelectSql(Object entity) {
        List<String> columns=new ArrayList<>();
        List<String> allcolumns=new ArrayList<>();
        List<Object> vs=new ArrayList<>();
        this.tableName =TableUtil.getTableName(entity);
        Map<String,Object> map=FieldUtil.getAllColumnName_Value(entity);
        Set<String> set=FieldUtil.getAllColumnName(entity);
        if(MapUtil.isEmpty(map)){
            log.error("取到的对象值为空...");
        }
        for(Map.Entry<String,Object> cv :map.entrySet()){
            columns.add(cv.getKey());
            vs.add(cv.getValue());
        }
        for(String column:set){
            allcolumns.add(column);
        }
        this.columnNames=columns;
        this.allColumnNames=allcolumns;
        this.values=vs;

    }

    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    @Override
    public List<Object> getValues() {
        return values;
    }

    /**
     *
     * @return
     *    SELECT ID,NAME,PHONE,ADDRESS FROM USER
     *    WHERE NAME=? AND PHONE=?
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("select ");
        for(String columnname:allColumnNames){
            SQL.append(columnname).append(",");
        }
        SQL.deleteCharAt(SQL.length()-1).append(" ").append("from").append(" ").append(tableName)
                .append("\n").append("   where").append(" ");
        for(int i=0;i<columnNames.size();++i){
            if(i==0) {
                SQL.append(columnNames.get(i)).append("=").append("?").append(" ");
            }else{
                SQL.append("and").append(" ").append(columnNames.get(i)).append("=").append("?").append(" ");
            }
        }
        log.info(" 生成的sql语句: "+SQL.toString());
        return SQL.toString();
    }
}
