package com.leopardframework.generator;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/13
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class GeneratorFactory {


    public static void openGenerator(Connection conn) throws SQLException {
        if(conn==null){
            throw new RuntimeException("获取数据库连接失败..");
        }
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        String[] tableType = { "TABLE" };
        ResultSet rs = databaseMetaData.getTables(null, null, "%",tableType);
        TableToJavaBean d = new TableToJavaBean();
        while(rs.next()){
            String tableName=rs.getString(3).toString();
            d.tableToBean(conn,tableName);
        }

    }
}
