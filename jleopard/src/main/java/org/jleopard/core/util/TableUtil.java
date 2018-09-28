package org.jleopard.core.util;



import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.jleopard.core.annotation.Table;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.ClassUtil;
import org.jleopard.util.StringUtil;

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
        String tableName = null;
        if (isTable(cls)){
            Table table=cls.getDeclaredAnnotation(Table.class);
            tableName=table.value();
            if(StringUtil.isEmpty(tableName)){
                tableName=cls.getSimpleName();
                tableName = tableName.substring(0,1).toLowerCase()+tableName.substring(1);
            }
        }
        return tableName;
    }

    /**
     * 获取实体对象上@Table注解的值
     * @param packagename  实体对象所在的包名 如:(org.jleopard.entity)
     * @return
     */
    public static List<String> getAllTableName(String packagename){
        List<String> tablenames=new ArrayList<>();
        Set<Class<?>> scls=ClassUtil.getClassSetByPackagename(packagename);
        scls = scls.stream().filter(clazz -> clazz.isAnnotationPresent(Table.class)).collect(Collectors.toSet());
        scls.stream().forEach(i->tablenames.add(getTableName(i)));
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
    public static List<String> showAllTableName(DataSource dataSource){
     
        try {
            Connection conn=dataSource.getConnection();
            return showAllTableName(conn);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

    }
}
