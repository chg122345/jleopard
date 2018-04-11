package com.leopardframework.core.util;

import com.leopardframework.core.annotation.Column;
import com.leopardframework.core.get.ColumnNameHelper;
import com.leopardframework.exception.NotfoundFieldException;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.util.ArrayUtil;
import com.leopardframework.util.StringUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class FieldUtil {

    private static final Log log=LogFactory.getLog(FieldUtil.class);

    /**
     *  获取 字段名 值  主用于insert  delete
     * @param entity
     *
     * @return   Map<String,Object>   key :对应的字段名   value : 相对应的值
     */
    public static Map<String,Object> getAllColumnName_Value(Object entity){
        Map<String,Object> c_v=new LinkedHashMap<>();
        String columnName;
        Object fieldValue;
        Class<?> cls=entity.getClass();
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields){
            if(!field.isAnnotationPresent(Column.class)){
                 continue;   //没有注解 不是我们要的对象  ignore
            }
            try {
                PropertyDescriptor pd=new PropertyDescriptor(field.getName(),cls);
                Method method=pd.getReadMethod();
                fieldValue=method.invoke(entity);

                if (fieldValue == null || "".equals(fieldValue) || (fieldValue instanceof Integer && (Integer) fieldValue == 0)
                        || (fieldValue instanceof Long && (Long) fieldValue == 0) || (fieldValue instanceof Double && (Double) fieldValue == 0.0)) {
                    continue;    //空值 不是我们所要的对象  ignore
                }
                columnName=ColumnNameHelper.getColumnName(field);
           //     System.out.println(columnName+" ");
                c_v.put(columnName,fieldValue);             //取到我们需要的打包
            } catch (Exception e) {
                log.error("getAllFieldName_Value  获取值失败..."+e);
               throw new RuntimeException("getAllFieldName_Value  获取值失败..."+e);
            }
        }

        return c_v;
    }

    /**
     *  除了主键 其余有值的都打包带走  主用于 update
     * @param entity
     * @return
     */
    public static Map<String,Object> getExceptPK_ColumnName_Value(Object entity){
        Map<String,Object> c_v=new LinkedHashMap<>();
        Class<?> cls=entity.getClass();
        String columnName;
        Object fieldValue;
        try {
        } catch (Exception e) {
            log.error("getAllFieldName_Value  实例化失败..",e);
            e.printStackTrace();
        }
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields){
            if(!field.isAnnotationPresent(Column.class)){
                 continue;       //没有注解 不是我们要的对象   ignore
            }
            Column column=field.getDeclaredAnnotation(Column.class);
            if(isPrimaryKey(column)>0/*column.isPrimary() == AUTOINCREMENT ||column.isPrimary()== YSE*/){
                 continue;   //是主键 不要
            }
            try {
                PropertyDescriptor pd=new PropertyDescriptor(field.getName(),cls);
                Method readmethod=pd.getReadMethod();
                fieldValue=readmethod.invoke(entity);
                if(fieldValue==null||"".equals(fieldValue)){
                    continue;    //空值 不是我们所要的对象  ignore
                }
                columnName=column.value().toUpperCase();//ColumnNameHelper.getColumnName(field);
                if(StringUtil.isEmpty(columnName)){
                    columnName=field.getName().toUpperCase();
                }
                c_v.put(columnName,fieldValue);             //取到我们需要的打包
            } catch (Exception e) {
                log.error("getAllFieldName_Value  获取值失败...");
               throw new RuntimeException("getAllFieldName_Value  获取值失败..."+e);
            }
        }

        return c_v;
    }

    /**
     * 获取所有的字段名  主用于 crtateTable select
     *
     * @param cls
     * @return
     */
    public static Set<String> getAllColumnName(Class<?> cls){
        Set<String> columns=new LinkedHashSet<>();
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;   //没有注解 不是我们要的对象  ignore
            }
            columns.add(ColumnNameHelper.getColumnName(field));
        }

        return columns;
    }


    public static Set<String> getAllColumnName(Object entity){
        return getAllColumnName(entity.getClass());
    }


    /**
     *  获取对应的数据库字段名 对应的Java类型
     * @param cls
     * @return   Map ( key : 对应的数据库字段名  value : 对应的Java类型 )
     */
    public static Map<String,Object> getAllColumnName_JavaType(Class<?> cls){
        Map<String,Object> colName_Type=new LinkedHashMap<>();
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;   //没有注解 不是我们要的对象  ignore
            }
            colName_Type.put(ColumnNameHelper.getColumnName(field),field.getType());
        }

        return colName_Type;
    }

    /**
     *   主要用于select
     *    对对象变量赋予数据库查出的值
     * @param cls 实体对象类
     * @return
     *   Map  key=数据库的字段名(@Column注解上的的值)，value=实体对象的成员变量名
     */
    public static Map<String,String> getColumnFieldName(Class<?> cls){
        Map<String,String> colnames=new HashMap<>();
        Field[] fields=cls.getDeclaredFields();
        if(ArrayUtil.isEmpty(fields)) {
            log.error(cls.getName()+"没有成员变量...");
            throw new NotfoundFieldException(cls+"没有成员变量...");
        }
        for(Field field:fields) {
            if(!field.isAnnotationPresent(Column.class)){
                continue;
            }
            String fieldName=field.getName();
            String colname=ColumnNameHelper.getColumnName(field);
            colnames.put(colname,fieldName);

        }
        return colnames;
    }

    /**
     * 获取所有主键
     * @param cls
     * @return list
     */
    public static List getPrimaryKeys(Class<?> cls){
        List pks =new ArrayList() ;
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields){
           Column column=field.getDeclaredAnnotation(Column.class);
           if(isPrimaryKey(column)>0){
               pks.add(ColumnNameHelper.getColumnName(field));
           }
        }
          return pks;
    }


    /**
     *  判断是否为主键  返回类型
     * @param column
     * @return    0: 不是主键 1 : 普通主键  2 : 自增主键
     */
    public static int isPrimaryKey( Column column){

        switch (column.isPrimary()) {
            case NO:
                return 0;
            case YSE:
                return 1;
            case AUTOINCREMENT:
                return 2;
            default:
                return 0;
        }
    }
}
