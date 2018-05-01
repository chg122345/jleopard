package com.leopardframework.generator;

import com.leopardframework.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/13
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *  存储字段类型信息  对应的Java类型   该导入的 java包
 */
class JavaTypeHelper {

    private static Map<String,String> map;   // key: columnType   value: javaType

    private static Map<String,String> imap;   // key: columnType   value: javaType

    static{
        map=new HashMap<>();
        imap=new HashMap<>();
        map.put("VARCHAR", "String");
        map.put("CHAR","String");
        map.put("BLOB","byte");
        map.put("TEXT","String");
        map.put("INT", "Integer");
        map.put("INTEGER", "Long");
        map.put("TINYINT","Integer");
        map.put("SMALLINT","Integer");
        map.put("MEDIUMINT","Integer");
        map.put("BIGINT","BigInteger");
        imap.put("BIGINT","import java.math.BigInteger");
        map.put("BIT","Boolean");
        map.put("FLOAT", "Float");
        map.put("DOUBLE", "Double");
        map.put("DECIMAL", "BigDecimal");
        imap.put("DECIMAL", "import java.math.BigDecimal");
        map.put("DATE", "Date");
        imap.put("DATE", "import java.sql.Date");
        map.put("YEAR", "Date");
        imap.put("YEAR", "import java.sql.Date");
        map.put("TIME", "Time");
        imap.put("TIME", "import java.sql.Time");
        map.put("TIMESTAMP", "Timestamp");
        imap.put("TIMESTAMP", "import java.sql.Timestamp");
        map.put("DATETIME", "Timestamp");
        imap.put("DATETIME", "import java.sql.Timestamp");
        map.put("TIMESTAMP_IMPORT", "Date");
        imap.put("TIMESTAMP_IMPORT", "import java.util.Date");
        map.put("DATETIME_IMPORT","Date");
        imap.put("DATETIME_IMPORT","import java.util.Date");
    }

    protected static String getImport(String dataType) {
        if (StringUtil.isEmpty(imap.get(dataType))) {
            return null;
        }else{
            return imap.get(dataType);
        }
    }

    protected static String getPojoType(String dataType) {
        StringTokenizer st = new StringTokenizer(dataType);
        return map.get(st.nextToken());
    }

}
