package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.session.Session;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 * @see SessionImplDirect
 * @ session管理   获取session
 */
public class SessionFactory {

    public static Session openSession(){
       /* return new SessionImplDirect();*/
        return new SessionDirectImpl();
    }
}
