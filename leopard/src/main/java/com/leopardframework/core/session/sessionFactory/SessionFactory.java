package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.Constant;
import com.leopardframework.core.session.SqlSession;
import com.leopardframework.loadxml.XmlFactoryBuilder;
import com.leopardframework.plugins.DBPlugin;
import com.leopardframework.plugins.c3p0.C3p0Plugin;

import java.sql.Connection;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 * @ session管理   获取session
 * 加载全局配置
 * @see SessionDirectImpl
 */
public final class SessionFactory {

    private static XmlFactoryBuilder.XmlFactory factory;

    private String xmlPath;

    private volatile static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(String xmlPath) {
            sessionFactory = new SessionFactory(xmlPath);
        return sessionFactory;
    }

    private SessionFactory(String xmlPath) {
        this.xmlPath = xmlPath;
    }

    public SqlSession openSession() {
        if (xmlPath.startsWith("classpath:")) {
            xmlPath = xmlPath.replace("classpath:", "").trim();
        }
        xmlPath = ClassLoader.getSystemResource(xmlPath).getPath();
        XmlFactoryBuilder builder = new XmlFactoryBuilder(xmlPath);
        factory = builder.getFactory();
        String packagePath = factory.getEntityPackage();
        return new SessionDirectImpl(packagePath);
    }

    static class Config {
        static Connection getConnection() {
            if (factory.hasBean(DBPlugin.class.getName())) {
                DBPlugin db = (DBPlugin) factory.getBean("dataSource");
                return db.getConn();
            } else if (factory.hasBean(C3p0Plugin.class.getName())) {
                C3p0Plugin c3p0 = (C3p0Plugin) factory.getBean("dataSource");
                return c3p0.getConn();
            }
            return null;
        }

        static boolean getDevModel() {
            return Constant.DEV;
        }

    }

}
