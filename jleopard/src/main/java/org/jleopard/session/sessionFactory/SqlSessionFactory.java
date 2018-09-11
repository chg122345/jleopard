package org.jleopard.session.sessionFactory;


import org.jleopard.core.util.ConfiguationUtil;
import org.jleopard.session.Configuration;
import org.jleopard.session.SqlSession;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 * @ session管理   获取session
 * 加载全局配置
 * @see SessionDirectImpl
 */
public final class SqlSessionFactory {

    private Configuration configuration;

    private SqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new SessionDirectImpl(configuration);
    }

    public static class Builder {

        private volatile static SqlSessionFactory sessionFactory;

        public static SqlSessionFactory build(Configuration configuration) {
            sessionFactory = new SqlSessionFactory(configuration);
            return sessionFactory;
        }

        public static SqlSessionFactory build(String xmlPath){
            return build(ConfiguationUtil.getConfiguration(xmlPath));
        }
    }

}
