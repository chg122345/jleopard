package org.jleopard.core.sql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.MapUtil;
import org.jleopard.util.PathUtils;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class SelectSql<T> implements Sql, CloumnNames, CloumnValue{

    private static final Log log=LogFactory.getLog(SelectSql.class);

    private String tableName;  //表名

    private List<String> columnNames; //字段名

    private List<String> allColumnNames; //所有字段名

    private List<Object> values;  //字段对应的值

    public SelectSql(T entity) {
        List<String> columns=new ArrayList<>();
        List<String> allcolumns=new ArrayList<>();
        List<Object> vs=new ArrayList<>();
        this.tableName =TableUtil.getTableName(entity);
        Map<String,Object> map=FieldUtil.getAllColumnName_Value(entity);
        Map<String, Field> ctMap=FieldUtil.getAllColumnName_Field(entity.getClass());
        if(MapUtil.isEmpty(map)){
            log.error("取到的对象值为空...");
        }
        map.forEach((c,v) -> {
        	columns.add(c);
        	vs.add(v);
        });
        ctMap.forEach((c,f) -> {
        	if (Collection.class.isAssignableFrom(f.getType())) {
				ParameterizedType pm = (ParameterizedType) f.getGenericType();
				Class<?> fcls = (Class<?>) pm.getActualTypeArguments()[0];
				if (!TableUtil.isTable(fcls)) {
					allcolumns.add(c);
				}
			}else {
				allcolumns.add(c);
			}
        });
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
        SQL.append("SELECT ");
        for(String columnname:allColumnNames){
            SQL.append(columnname).append(",");
        }
        SQL.deleteCharAt(SQL.length()-1).append(" ").append("FROM").append(" ").append(tableName)
                .append(PathUtils.LINE).append("   WHERE").append(" ");
        for(int i=0;i<columnNames.size();++i){
            if(i==0) {
                SQL.append(columnNames.get(i)).append("=").append("?").append(" ");
            }else{
                SQL.append("AND").append(" ").append(columnNames.get(i)).append("=").append("?").append(" ");
            }
        }
        log.info(" 生成的sql语句: "+SQL.toString());
        return SQL.toString();
    }
}
