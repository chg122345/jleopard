package com.leopardframework.generator;

import com.leopardframework.core.Factory;
import com.leopardframework.core.session.Session;
import com.leopardframework.loadxml.XmlFactoryBuilder;
import com.leopardframework.plugins.DBPlugin;
import com.leopardframework.plugins.c3p0.C3p0Plugin;

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
public class GeneratorFactory implements Factory {

    private Connection conn;
    private String xmlPath;

    public GeneratorFactory(String xmlPath) {
        this.xmlPath=xmlPath;
    }

    @Override
    public Session openSession() {
        return null;
    }

    public void openGenerator() throws SQLException {
        if(xmlPath.startsWith("classpath:")){
            xmlPath= xmlPath.replace("classpath:","").trim();
        }
        xmlPath=ClassLoader.getSystemResource(xmlPath).getPath();
        XmlFactoryBuilder builder = new XmlFactoryBuilder(xmlPath);
        XmlFactoryBuilder.XmlFactory factory = builder.getFactory();
        if(factory.hasBean(DBPlugin.class.getName())){
            DBPlugin db=(DBPlugin)factory.getBean("dataSource");
            this.conn= db.getConn();
        }else if(factory.hasBean(C3p0Plugin.class.getName())){
            C3p0Plugin c3p0= (C3p0Plugin) factory.getBean("dataSource");
            this.conn=c3p0.getConn();
        }
        if(conn==null){
            throw new RuntimeException("获取数据库连接失败..");
        }
        DatabaseMetaData databaseMetaData = conn.getMetaData();
        String[] tableType = { "TABLE" };
        ResultSet rs = databaseMetaData.getTables(null, null, "%",tableType);
        TableToJavaBean d = new TableToJavaBean();
        while(rs.next()){
            String tableName=rs.getString(3).toString();
            d.tableToBean(conn,tableName,factory.getEntityPackage(),factory.getGeneratorPackage());
        }

    }
}
