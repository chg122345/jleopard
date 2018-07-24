package org.jleopard.core.util;



import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.exception.NotFoundFieldException;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.session.sessionFactory.ColumnNameHelper;
import org.jleopard.util.ArrayUtil;
import org.jleopard.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class FieldUtil {

    private static final Log log=LogFactory.getLog(FieldUtil.class);

    /**
     *  获取 字段名 值  主用于insert  delete  (2018-4-18 添加外键的值)
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
                PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
                Method method = pd.getReadMethod();
                if (TableUtil.isTable(field.getType())) {  //判断是否为我们所需的对象类
                    fieldValue = method.invoke(entity);
                    if (fieldValue == null) {
                        continue;
                    } else {
                        String fname = FieldUtil.getPrimaryKeys(field.getType()).get(0);
                        fieldValue = getAllColumnName_Value(fieldValue).get(fname);  //外连接表的主键值
                    }
                }else{
                    fieldValue = method.invoke(entity);
                }
                if (fieldValue == null || "".equals(fieldValue) || (fieldValue instanceof Integer && (Integer) fieldValue == 0)
                        || (fieldValue instanceof Long && (Long) fieldValue == 0) || (fieldValue instanceof Double && (Double) fieldValue == 0.0)) {
                    continue;    //空值 不是我们所要的对象  ignore
                }
                columnName = ColumnNameHelper.getColumnName(field);
                //     System.out.println(columnName+" ");
                c_v.put(columnName, fieldValue);             //取到我们需要的打包
            } catch (Exception e) {
                log.error("getAllFieldName_Value  获取值失败..." + e);
                throw new RuntimeException("getAllFieldName_Value  获取值失败..." + e);
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
                Method readMethod=pd.getReadMethod();
                if (TableUtil.isTable(field.getType())) {  //判断是否为我们所需的对象类
                    fieldValue = readMethod.invoke(entity);
                    fieldValue=getAllColumnName_Value(fieldValue).get(FieldUtil.getPrimaryKeys(field.getType()).get(0));  //外连接表的主键值
                }else{
                    fieldValue = readMethod.invoke(entity);
                }
                if (fieldValue == null || "".equals(fieldValue) || (fieldValue instanceof Integer && (Integer) fieldValue == 0)
                        || (fieldValue instanceof Long && (Long) fieldValue == 0) || (fieldValue instanceof Double && (Double) fieldValue == 0.0)) {
                    continue;    //空值 不是我们所要的对象  ignore
                }
                columnName = ColumnNameHelper.getColumnName(field);
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
        Map<String,String> colNames=new HashMap<>();
        Field[] fields=cls.getDeclaredFields();
        if(ArrayUtil.isEmpty(fields)) {
            log.error(cls.getName()+"没有成员变量...");
            throw new NotFoundFieldException(cls+"没有成员变量...");
        }
        for(Field field:fields) {
            if(!field.isAnnotationPresent(Column.class)){
                continue;
            }
            String fieldName=field.getName();
            String colName=ColumnNameHelper.getColumnName(field);
            colNames.put(colName,fieldName);

        }
        return colNames;
    }

    /**
     * 获取所有主键
     * @param cls
     * @return list
     */
    public static List<String> getPrimaryKeys(Class<?> cls){
        List<String> pks =new ArrayList<String>() ;
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
     *  获取主键列名 类型  key: 主键列名 value : 主键对应的类型
     * @param cls
     * @return  map
     */
    public static Map<String,Class<?>> getPrimaryKeys_Type(Class<?> cls){
        Map<String,Class<?>> pk_t =new HashMap<>();
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields){
            Column column=field.getDeclaredAnnotation(Column.class);
            if(isPrimaryKey(column)>0){
                pk_t.put(ColumnNameHelper.getColumnName(field),field.getType());
            }
        }
        return pk_t;
    }

   /* public static Map<String,Object> getPrimaryKeys_Values(Class<?> cls){
        Map<String,Object> pk_v =new HashMap<>();
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields){
            Column column=field.getDeclaredAnnotation(Column.class);
            if(isPrimaryKey(column)>0){
             //   pk_t.put(ColumnNameHelper.getColumnName(field),field.getType());
            }
        }
        return pk_v;
    }*/
   public static List<String> getForeignKeyName(Class<?> cls){
       List<String> fkNames =new ArrayList<String>() ;
       Field[] fields=cls.getDeclaredFields();
       for (Field field :fields){
           Column column=field.getDeclaredAnnotation(Column.class);
           String fName=column.relation();
          if (StringUtil.isNotEmpty(fName)){
              fkNames.add(column.value());
          }
       }
       return fkNames;
   }
    /**
     *  获取外键信息  返回map key: 外键名称  value: 外键关联表的类
     * @param cls
     * @return  map
     */
    public static Map<String,Class<?>> getForeignKeys(Class<?> cls){
        Map<String,Class<?>> fks=new HashMap<>();
        Field[] fields=cls.getDeclaredFields();
        for (Field field :fields){
            Column column=field.getDeclaredAnnotation(Column.class);
            String foreignKeyName=column.relation();
           if(StringUtil.isEmpty(foreignKeyName)){
               continue;
           }
           Class<?> clazz=field.getType();   //  外键对应的类
            fks.put(column.value(),clazz);

        }
        return fks;
    }

    /**
     *  判断是否为主键  返回类型
     * @param column
     * @return    0: 不是主键 1 : 普通主键  2 : 自增主键
     */
    public static int isPrimaryKey( Column column){

        if(column==null){
            throw new NotFoundFieldException("没有找到@Column注解...");
        }
        switch (column.isPrimary()) {
            case NO:
                return EnumPrimary.NO.getCode();
            case YES:
                return EnumPrimary.YES.getCode();
            case AUTOINCREMENT:
                return EnumPrimary.AUTOINCREMENT.getCode();
            default:
                return 0;
        }
    }
}
