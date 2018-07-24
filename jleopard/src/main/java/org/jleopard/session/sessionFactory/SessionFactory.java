package org.jleopard.session.sessionFactory;



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
public final class SessionFactory {

    private Configuration configuration;

    private volatile static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory(Configuration configuration) {
            sessionFactory = new SessionFactory(configuration);
        return sessionFactory;
    }

    private SessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    public SqlSession openSession() {
        return new SessionDirectImpl(configuration);
    }


}
