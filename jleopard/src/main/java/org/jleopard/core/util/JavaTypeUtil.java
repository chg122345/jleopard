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
          sqlType="VARCHAR(255)";
      }else if(javaType==byte.class || javaType == java.lang.Byte.class){
          sqlType="TINYINT";
      }else if(javaType==java.lang.Long.class){
          sqlType="INTEGER";
      }else if(javaType==BigInteger.class){
          sqlType="BIGINT";
      }else if(javaType==java.lang.Integer.class||javaType==int.class||javaType==long.class){
          sqlType="INT";
      }else if(javaType==java.lang.Boolean.class||javaType==boolean.class){
          sqlType="BIT";
      }else if(javaType==java.lang.Float.class){
          sqlType="FLOAT";
      }else if(javaType==java.lang.Double.class){
          sqlType="DOUBLE";
      }else if(javaType==BigDecimal.class){
          sqlType="DECIMAL";
      }else if(javaType==java.util.Date.class||javaType==java.sql.Date.class||javaType==java.sql.Time.class){
          sqlType="DATETIME";
      }else if(javaType==java.sql.Timestamp.class){
          sqlType="TIMESTAMP";
      }

       return sqlType;
   }
}
