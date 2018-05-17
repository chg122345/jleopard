package com.leopardframework.core;

import com.leopardframework.core.session.sessionFactory.SessionFactory;
import com.leopardframework.generator.GeneratorFactory;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/15
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public final class Factory {

    public static SessionFactory getSessionFactory(String xmlPath) {
      return SessionFactory.getSessionFactory(xmlPath);
    }

    public static GeneratorFactory getGeneratorFactory(String xmlPath) {
        return GeneratorFactory.getGeneratorFactory(xmlPath);
    }
}
