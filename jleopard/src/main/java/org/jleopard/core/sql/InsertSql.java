package org.jleopard.core.sql;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class InsertSql implements Sql,CloumnNames,CloumnValue{

    private static final Log log=LogFactory.getLog(InsertSql.class);

    private String tableName;  //表名

    private List<String> columnNames; //字段名

    private List<Object> values;  //字段对应的值

  //  private Map<String,Object> C_V;   //字段名==>值

    /**
     *
     * @param tableName
     * @param map
     */
    public InsertSql(String tableName,Map<String,Object> map) {
        List<String> columns=new ArrayList<>();
        List<Object> vs=new ArrayList<>();
        this.tableName = tableName;
        for(Map.Entry<String,Object> cv :map.entrySet()){
            columns.add(cv.getKey());
           vs.add(cv.getValue());
        }
        this.columnNames=columns;
        this.values=vs;
     //   System.out.println(columnNames.size());
    }


    /**
     *
     */
    public InsertSql(Object entity) {
        List<String> columns=new ArrayList<>();
        List<Object> vs=new ArrayList<>();
        this.tableName =TableUtil.getTableName(entity);
        Map<String,Object> map=FieldUtil.getAllColumnName_Value(entity);
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

    /**
     *
     * @return
     *    字段名
     */
    @Override
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * @return
     *   字段名对应的 value
     */
    @Override
    public List<Object> getValues() {
        return values;
    }

    /**
     *  获取拼接好的sql
     * @return
     *     例:  INSERT INTO USER(PHONE,ID,NAME)
     *              VALUES(?,?,?)
     */
    @Override
    public String getSql(){
        StringBuilder SQL =new StringBuilder();
        SQL.append("insert into ").append(tableName).append("(");
        for(int i=0;i<columnNames.size();++i){
          SQL.append(columnNames.get(i)).append(",");
        }
        SQL.deleteCharAt(SQL.length()-1).append(")").append(" \n    ").append("values").append("(");
        for(int i=0;i<columnNames.size();++i){
            SQL.append("?").append(",");
        }
        SQL.deleteCharAt(SQL.length()-1).append(")");
        log.info(" 生成的sql语句: "+SQL.toString());
        return SQL.toString();
    }

}
