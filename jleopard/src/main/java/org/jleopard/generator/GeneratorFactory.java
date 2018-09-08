package org.jleopard.generator;


import org.jleopard.core.util.ConfiguationUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.session.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/13
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *   运作逆向工程的工厂 单列模式
 */
public final class GeneratorFactory {

    private static final Log LOG=LogFactory.getLog(GeneratorFactory.class);
    
    private static String separator = System.getProperty("file.separator");
    private Configuration configuration;
    private DataSource dataSource;
    private Connection conn;

    private GeneratorFactory(Configuration configuration) {
    	this.configuration = configuration;
        this.dataSource = configuration.getDataSource();
    }


    /**
     *   执行逆向工程
     * @throws SQLException
     */
    public void openGenerator() throws SQLException {
      this.conn = dataSource.getConnection();
        if(conn==null){
            LOG.error("获取数据库连接失败..");
            throw new RuntimeException("获取数据库连接失败..");
        }
        TableToJavaBean tableToJavaBean = new TableToJavaBean();
        String generatorPackage=configuration.getGeneratorPackage();
        String generatorProject=configuration.getGeneratorProject();
        if(!generatorProject.startsWith("/")){
            generatorProject="/"+generatorProject;
        }
        if(!generatorProject.endsWith("/")){
           generatorProject=generatorProject+"/";
        }
        generatorProject.replace("/", separator);
        for (String tableName:TableUtil.showAllTableName(conn)){
            tableToJavaBean.tableToBean(conn,tableName,generatorPackage,generatorProject);
        }
        LOG.info("Success ! 工程目标路径 : "+generatorProject+generatorPackage);
    }

    public static class Builder {

        private volatile static GeneratorFactory generatorFactory;

        public static GeneratorFactory build(Configuration configuration){
            if (generatorFactory==null){
                synchronized (GeneratorFactory.class){
                    if (generatorFactory==null){
                        generatorFactory = new GeneratorFactory(configuration);
                    }
                }
            }
            return generatorFactory;
        }

        public static GeneratorFactory build(String xmlPath){
            return build(ConfiguationUtil.getConfiguration(xmlPath));
        }
    }
}
