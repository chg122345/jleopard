package com.leopardframework.core;

import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public final class SqlBuilder {

     private static final Log log=LogFactory.getLog(SqlBuilder.class);
    /**
     * 通过反射取对象的成员变量的注解 @Table 获得到对应数据库表名
     * 获取成员变量的值
     * 生成 sql :   insert into tableName(columnName1,columnName2)
     *                    values(fieldValue1,fieldValue2)
     * @param object   传入一个实体对象   entity
     *
     * @return    Map  key : 动态sql语句
     *                 value : 拼装的sql语句的值 (? ==> value)
     */
   /* public static Map<String,List<Object>> getSaveSqlValues(Object object){
        Map<String,List<Object>> SqlValues=new HashMap<>();
        StringBuilder SQL=new StringBuilder();
        Class<?> cls=object.getClass();
        List<Object> fieldValues=new ArrayList<Object>();
        boolean exist=cls.isAnnotationPresent(Table.class);
        if(!exist){
            log.error(object+"没有@Table 注解");
            throw new SqlBuilderException(object+"不是要操作的对象, 没有@Table 注解....");

        }
       String tableName=TableUtil.getTableName(cls);
        SQL.append("insert into ").append(tableName ).append("(");                                      *//**** SQL  ***//*
        Field[] fields=cls.getDeclaredFields();
        if(ArrayUtil.isEmpty(fields)){
            log.error(object+"没有成员变量");
            throw new NotfoundFieldException(object+" 没有成员变量...");
        }
        String columnName=null;
        for(Field field:fields){
            boolean fexist=field.isAnnotationPresent(Column.class);
            if(!fexist){
               continue;
            }
            columnName=ColumnNameHelper.getColumnName(field);
            Object value=FieldUtil.getFieldValue(object,field);
            if(value!=null||"".equals(value)){
                fieldValues.add(value);
                SQL.append(columnName).append(",");                                                         *//**** SQL  ***//*
            }

        }
        SQL.deleteCharAt(SQL.length()-1).append(")").append("\n");                                                   *//**** SQL  ***//*
        SQL.append(" ").append("values").append("(");
        for (int i=0;i<fieldValues.size();++i){
            SQL.append("?").append(",");                                                                             *//**** SQL  ***//*
        }
            SQL.deleteCharAt(SQL.length()-1).append(")");                                                            *//**** SQL  ***//*
        SqlValues.put(SQL.toString(),fieldValues);
        log.info("生成的Sql语句："+SQL.toString());
        return SqlValues;
    }

    *//**
     *  得到数据库表名
     *  生成不完整的sql  delete from tableName
     * @param object
     * @return
     *//*
    public static String getDeleteSql(Object object){
        StringBuilder SQL=new StringBuilder();
        Class<?> cls=object.getClass();
        boolean exist=cls.isAnnotationPresent(Table.class);
        if(!exist){
            log.error(object+"没有@Table 注解");
            throw new SqlBuilderException(object+"不是要操作的对象, 没有@Table 注解....");

        }
        String tableName=TableName.getTableName(cls);
        SQL.append("delete from ").append(tableName );
        return SQL.toString();
    }

    *//**
     * 通过反射取对象的成员变量的注解 @Table 获得到对应数据库表名
     * 并获取成员变量的值
     * 生成 sql :   delete from tableName where columnName1=? and columnName2=?
     *
     * @param object   传入一个实体对象   entity
     *
     * @return    Map  key : 动态sql语句
     *                 value : 拼装的sql语句的值 (? ==> value)
     *//*
    public static Map<String,List<Object>> getDeleteSqlValues(Object object){
        Map<String,List<Object>> SqlValues=new HashMap<>();
        StringBuilder SQL=new StringBuilder();
        Class<?> cls=object.getClass();
        List<Object> fieldValues=new ArrayList<Object>();
        boolean exist=cls.isAnnotationPresent(Table.class);
        if(!exist){
            log.error(object+"没有@Table 注解");
            throw new SqlBuilderException(object+"不是要操作的对象, 没有@Table 注解.....");

        }
        String tableName=TableName.getTableName(cls);
        SQL.append("delete from ").append(tableName ).append("\n").append("  where").append(" ");                                      *//**** SQL  ***//*
        Field[] fields=cls.getDeclaredFields();
        if(ArrayUtil.isEmpty(fields)){
            log.error(object+"没有成员变量");
            throw new NotfoundFieldException(object+" 没有成员变量...");
        }
        String columnName=null;
        for(int i=0;i<fields.length;++i*//*Field field:fields*//*){
            boolean fexist=fields[i].isAnnotationPresent(Column.class);
            if(!fexist){
                continue;
            }
            columnName=ColumnNameHelper.getColumnName(fields[i]);
            Object value=FieldValue.getFieldValue(object,fields[i]);
            if(value!=null||"".equals(value)){
                fieldValues.add(value);
                if(i==0){
                    SQL.append(columnName).append("=").append("?").append(" ");                                                         *//**** SQL  ***//*
                } else{
                    SQL.append("and").append(" ").append(columnName).append("=").append("?").append(" ");                              *//**** SQL  ***//*
                }
            }

        }
        SqlValues.put(SQL.toString(),fieldValues);
        log.info("生成的Sql语句："+SQL.toString());
        return SqlValues;
    }

    *//**
     *
     * @param object
     * @return
     *//*
    public static Map<String,List<Object>> getUpdateSqlValues(Object object){
        Map<String,List<Object>> SqlValues=new HashMap<>();
        StringBuilder SQL=new StringBuilder();
        Class<?> cls=object.getClass();
        List<Object> fieldValues=new ArrayList<>();
        boolean exist=cls.isAnnotationPresent(Table.class);
        if(!exist){
            log.error(object+"没有@Table 注解");
            throw new SqlBuilderException(object+"不是要操作的对象, 没有@Table 注解....");

        }
        String tableName=TableName.getTableName(cls);
        SQL.append("update ").append(tableName ).append(" ");                                      *//**** SQL  ***//*
        Field[] fields=cls.getDeclaredFields();
        if(ArrayUtil.isEmpty(fields)){
            log.error(object+"没有成员变量");
            throw new NotfoundFieldException(object+" 没有成员变量...");
        }
        String columnName=null;
        String primaryKeyName=PrimaryKeyName.getPrimaryKeyName(cls);
        for(int i=0;i<fields.length;++i*//*Field field:fields*//*){
            boolean fexist=fields[i].isAnnotationPresent(Column.class);
            if(!fexist){
                continue;
            }
            columnName=ColumnNameHelper.getColumnName(fields[i]);
            Object value=FieldValue.getFieldValue(object,fields[i]);
            if(value!=null||"".equals(value)){
                fieldValues.add(value);
                if(columnName.equals(primaryKeyName)){
                    continue;
                }
                SQL.append("set ").append(columnName).append("=?").append(" ");                                                         *//**** SQL  ***//*
            }

        }
        SQL.deleteCharAt(SQL.length()-1).append(")");                                                            *//**** SQL  ***//*
        SqlValues.put(SQL.toString(),fieldValues);
        log.info("生成的Sql语句："+SQL.toString());
        return SqlValues;
    }*/

}
