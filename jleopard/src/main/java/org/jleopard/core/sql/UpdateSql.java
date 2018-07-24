package org.jleopard.core.sql;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.MapUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class UpdateSql implements Sql, CloumnNames, CloumnValue{

    private static final Log log=LogFactory.getLog(UpdateSql.class);

    private String tableName;  //表名

    private List<String> columnNames; //字段名

    private List<Object> values;  //字段对应的值

    public UpdateSql(Object entity) {
        List<String> columns=new ArrayList<>();
        List<Object> vs=new ArrayList<>();
        this.tableName =TableUtil.getTableName(entity);
        Map<String,Object> map=FieldUtil.getExceptPK_ColumnName_Value(entity);
        if(MapUtil.isEmpty(map)){
            log.error("取到的对象值为空...");
        }
        for(Map.Entry<String,Object> cv :map.entrySet()){
            columns.add(cv.getKey());
            vs.add(cv.getValue());
        }
        this.columnNames=columns;
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
     *    UPDATE USER
     *  SET NAME=?,PHONE=?
     *    WHERE
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("update ").append(tableName).append(" \n").append(" ").append("set").append(" ");
        for(int i=0;i<columnNames.size();++i){
            SQL.append(columnNames.get(i)).append("=?").append(",");
        }
        SQL.deleteCharAt(SQL.length()-1).append("\n").append("   where").append(" ");
        log.info(" 生成的sql语句: "+SQL.toString()/*.toUpperCase()*/);
        return SQL.toString()/*.toUpperCase()*/;
    }
}
