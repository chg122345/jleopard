package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.Constant;
import com.leopardframework.core.session.Session;
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
 * @see SessionDirectImpl
 * @ session管理   获取session
 *   加载全局配置
 */
public class SessionFactory {

private static XmlFactoryBuilder.XmlFactory factory;

    public static Session openSession(String xmlpath){
        if(xmlpath.startsWith("classpath:")){
          xmlpath= xmlpath.replace("classpath:","").trim();
        }
       xmlpath=ClassLoader.getSystemResource(xmlpath).getPath();
        XmlFactoryBuilder builder = new XmlFactoryBuilder(xmlpath);
       /* XmlFactoryBuilder.XmlFactory */ factory = builder.getFactory();
         String packagePath=factory.getEntityPackage();
        return new SessionDirectImpl(packagePath);
    }

    public static class Config{
        public  static Connection getConnection(){
            if(factory.hasBean(DBPlugin.class.getName())){
                DBPlugin db=(DBPlugin)factory.getBean("dataSource");
                return db.getConn();
            }else if(factory.hasBean(C3p0Plugin.class.getName())){
                C3p0Plugin c3p0= (C3p0Plugin) factory.getBean("dataSource");
                return c3p0.getConn();
            }
            return null;
        }

        public static boolean getDevModel(){
            return Constant.DEV;
        }

        public static String getEntityPackage(){

            return factory.getEntityPackage();
        }

        public static String getGeneratorPackage(){

            return factory.getGeneratorPackage();
        }
    }

}
