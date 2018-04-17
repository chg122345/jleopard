package com.leopardframework.core.sql;

import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.util.MapUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class DeleteSql implements Sql{

    private static final Log log=LogFactory.getLog(DeleteSql.class);

    private String tableName;  //表名

    private List<String> columnNames; //字段名

    private List<Object> values;  //字段对应的值

        public DeleteSql(Object entity){
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

    @Override
    public List<String> getColumnNames() {
        return this.columnNames;
    }

    @Override
    public List<Object> getValues() {
        return this.values;
    }

    /**
     *
     *   生成的sql
     * @return
     *     DELETE FROM USER
     *        WHERE ID=? AND NAME=? AND PHONE=? AND ADDRESS=?
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("delete from ").append(tableName).append("\n").append(" ").append("where ");
        for(int i=0;i<columnNames.size();++i){
            if(i==0) {
                SQL.append(columnNames.get(i)).append("=?").append(" ");
            }else{
                SQL.append("and").append(" ").append(columnNames.get(i)).append("=?").append(" ");
            }
        }
        log.info(" 生成的sql语句: "+SQL.toString()/*.toUpperCase()*/);
        return SQL.toString()/*.toUpperCase()*/;
    }
}
