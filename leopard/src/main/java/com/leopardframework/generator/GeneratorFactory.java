package com.leopardframework.generator;

import com.leopardframework.core.util.TableUtil;
import com.leopardframework.loadxml.XmlFactoryBuilder;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.plugins.DBPlugin;
import com.leopardframework.plugins.c3p0.C3p0Plugin;
import com.leopardframework.util.ClassUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/13
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *   运作逆向工程的工厂 单列模式
 */
public final class GeneratorFactory {

    private static final Log LOG=LogFactory.getLog(GeneratorFactory.class);

    private Connection conn;
    private String xmlPath;
    private volatile static GeneratorFactory generatorFactory;

    public static GeneratorFactory getGeneratorFactory(String xmlPath){
        if (generatorFactory==null){
            synchronized (GeneratorFactory.class){
                if (generatorFactory==null){
                    generatorFactory=new GeneratorFactory(xmlPath);
                }
            }
        }
        return generatorFactory;
    }

    private GeneratorFactory(String xmlPath) {
        this.xmlPath=xmlPath;
    }


    /**
     *   执行逆向工程
     * @throws SQLException
     */
    public void openGenerator() throws SQLException {
        if(xmlPath.startsWith("classpath:")){
            xmlPath= xmlPath.replace("classpath:","").trim();
        }
        xmlPath=ClassUtil.getClassLoader().getResource(xmlPath).getPath();
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
            LOG.error("获取数据库连接失败..");
            throw new RuntimeException("获取数据库连接失败..");
        }
        TableToJavaBean tableToJavaBean = new TableToJavaBean();
        String generatorPackage=factory.getGeneratorPackage();
        String generatorProject=factory.getGeneratorProject();
        if(!generatorProject.startsWith("\\")){
            generatorProject="\\"+generatorProject;
        }
        if(!generatorProject.endsWith("\\")){
           generatorProject=generatorProject+"\\";
        }
        for (String tableName:TableUtil.showAllTableName(conn)){
            tableToJavaBean.tableToBean(conn,tableName,generatorPackage,generatorProject);
        }
        System.out.println("Success ! 工程目标路径 : "+generatorProject+generatorPackage);
    }
}
