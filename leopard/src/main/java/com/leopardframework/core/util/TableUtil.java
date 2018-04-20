package com.leopardframework.core.util;

import com.leopardframework.core.annotation.Table;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.plugins.c3p0.C3p0Plugin;
import com.leopardframework.util.ClassUtil;
import com.leopardframework.util.StringUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class TableUtil {

    private static final Log LOG =LogFactory.getLog(TableUtil.class);
    /**
     *   获取对象对应的表名
     * @param entity
     * @return
     */
    public static String getTableName(Object entity){
        Class<?> cls=entity.getClass();

        return getTableName(cls);
    }

    public static String getTableName(Class<?> cls){
        Table table=cls.getDeclaredAnnotation(Table.class);
        String tableName=table.value()/*.toUpperCase()*/;
        if(StringUtil.isEmpty(tableName)){
            tableName=cls.getSimpleName()/*.toUpperCase()*/;
        }
        return tableName.substring(0,1).toLowerCase()+tableName.substring(1);
    }

    /**
     * 获取实体对象上@Table注解的值
     * @param packagename  实体对象所在的包名 如:(gxf.orm.model)
     * @return
     */
    public static List<String> getAllTableName(String packagename){
        List<String> tablenames=new ArrayList<>();
        Set<Class<?>> scls=ClassUtil.getClassSetByPackagename(packagename);
        for(Class<?> cls:scls) {
               if(!isTable(cls)){
                   continue;
               }
            tablenames.add(getTableName(cls));
        }
        return tablenames;
    }

    /**
     * 判断是否为我们所需要的对象
     *  是否有@ Table注解
     * @param cls
     * @return
     */
    public static boolean isTable(Class<?> cls){
        return cls.isAnnotationPresent(Table.class);
    }
    public static boolean isTable(Object entity){
        return isTable(entity.getClass());
    }

    /**
     * 读取数据库已存在的表名
     * @param conn 连接对象
     * @return
     */

    public static List<String> showAllTableName(Connection conn){
        List<String> tablenames=new ArrayList<>();
        if(conn==null) {
            LOG.error("获取数据库连接失败..");
            throw new RuntimeException("获取数据库连接失败..");
        }else {
            try {
                String[] tableType = { "TABLE" };
                DatabaseMetaData dmd=(DatabaseMetaData) conn.getMetaData();
                ResultSet res=dmd.getTables(null, null, "%", tableType);
                while(res.next()) {
                    tablenames.add(res.getString("TABLE_NAME"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }
        return tablenames;
    }
    public static List<String> showAllTableName(C3p0Plugin c3p0){
        if(!c3p0.start()) {
            c3p0.start();
        }
        try {
            Connection conn=c3p0.getDataSource().getConnection();
            return showAllTableName(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
