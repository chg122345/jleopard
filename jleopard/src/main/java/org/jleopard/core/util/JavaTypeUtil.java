package org.jleopard.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/11
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *  java类型转数据库对应的类型
 */
public class JavaTypeUtil {

   public static String getSqlType(Object javaType){
       String sqlType=null;
      if(javaType==java.lang.String.class){
          sqlType="varchar(255)";
      }else if(javaType==byte.class){
          sqlType="blob";
      }else if(javaType==java.lang.Long.class){
          sqlType="integer";
      }else if(javaType==BigInteger.class){
          sqlType="bigint";
      }else if(javaType==java.lang.Integer.class||javaType==int.class||javaType==long.class){
          sqlType="int";
      }else if(javaType==java.lang.Boolean.class||javaType==boolean.class){
          sqlType="bit";
      }else if(javaType==java.lang.Float.class){
          sqlType="float";
      }else if(javaType==java.lang.Double.class){
          sqlType="double";
      }else if(javaType==BigDecimal.class){
          sqlType="decimal";
      }else if(javaType==java.util.Date.class||javaType==java.sql.Date.class||javaType==java.sql.Time.class){
          sqlType="datetime";
      }else if(javaType==java.sql.Timestamp.class){
          sqlType="timestamp";
      }

       return sqlType;
   }
}
