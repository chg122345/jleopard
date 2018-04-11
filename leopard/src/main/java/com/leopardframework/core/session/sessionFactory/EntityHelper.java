package com.leopardframework.core.session.sessionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/10
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *  对查询出的对象赋值
 */
final class EntityHelper {
    protected static List invoke(ResultSet res, Class<?> cls, Map<String, String> C_F) throws IllegalAccessException, InstantiationException, IntrospectionException, SQLException, InvocationTargetException {
        List entitys=new ArrayList();
        while (res.next()) {
            Object entity=cls.newInstance();
            for (Map.Entry<String, String> cf : C_F.entrySet()) {
                PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls);
                Method write = pd.getWriteMethod();
                write.invoke(entity, res.getObject(cf.getKey()));
            }
            entitys.add(entity);
        }
        return entitys;
    }
}
