package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.util.CollectionUtil;

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

    /**
     *   查询出的结果赋值给相应对象  不考虑外键值
     *      外键的值为空  不赋值
     * @param res   查出的结果集
     * @param cls   赋值对象类
     * @param C_F
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws IntrospectionException
     * @throws SQLException
     * @throws InvocationTargetException
     */
    protected static <T> List<T> invoke(ResultSet res, Class<T> cls, Map<String, String> C_F) throws IllegalAccessException, InstantiationException, IntrospectionException, SQLException, InvocationTargetException {
        List entitys=new ArrayList();
        while (res.next()) {
            Object entity=cls.newInstance();
            for (Map.Entry<String, String> cf : C_F.entrySet()) {
                PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls);
                Method write = pd.getWriteMethod();
                Map<String,Class<?>> fns=FieldUtil.getForeignKeys(cls);
                if (CollectionUtil.isNotEmpty(fns)) {
                    for (Map.Entry<String, Class<?>> fn : fns.entrySet()) {
                        if (cf.getKey().equals(fn.getKey())) {
                            Object fkv = res.getObject(cf.getKey());   //外键值
                            Class<?> clazz = fn.getValue();
                            Object entity2 = clazz.newInstance();
                            PropertyDescriptor pd2 = new PropertyDescriptor(FieldUtil.getColumnFieldName(clazz).get(FieldUtil.getPrimaryKeys(clazz).get(0)), clazz);
                            Method write2 = pd2.getWriteMethod();
                            write2.invoke(entity2, fkv);
                            write.invoke(entity, entity2);   // 外表值设上
                        } else {
                            write.invoke(entity, res.getObject(cf.getKey()));
                        }
                    }
                }else{
                    write.invoke(entity, res.getObject(cf.getKey()));
                }
            }
            entitys.add(entity);
        }
        return entitys;
    }

    /**
     *   查询出的结果赋值给相应对象 (注 : 只赋值给有外键的对象)
     *     返回的是一个完整数据的对象 ..
     * @param res   数据库查出的结果集
     * @param cls1  带外键的对象类
     * @param cls2  属于对象一的外键类
     * @param <T>
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     * @throws InvocationTargetException
     * @throws IntrospectionException
     */
    protected static <T> List<T> invoke(ResultSet res, Class<T> cls1,Class<?> cls2) throws IllegalAccessException, InstantiationException, SQLException, InvocationTargetException, IntrospectionException {
        List entitys=new ArrayList();
        String tableName1=TableUtil.getTableName(cls1);
        String tableName2=TableUtil.getTableName(cls2);
        while (res.next()) {
            Object entity=cls1.newInstance();
            for (Map.Entry<String, String> cf : FieldUtil.getColumnFieldName(cls1).entrySet()) {
                PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls1);
                Method write = pd.getWriteMethod();
                List<String> fns=FieldUtil.getForeignKeyName(cls1);
                for (String fn : fns) {
                    if (cf.getKey().equals(fn)) {
                         Object entity2=cls2.newInstance();
                        for (Map.Entry<String, String> cf2 : FieldUtil.getColumnFieldName(cls2).entrySet()){
                            PropertyDescriptor pd2 = new PropertyDescriptor(cf2.getValue(), cls2);
                            Method write2 = pd2.getWriteMethod();
                            write2.invoke(entity2,res.getObject(tableName2+"."+cf2.getKey()));
                        }
                         write.invoke(entity,entity2);   // 外表值设上
                    } else {
                        write.invoke(entity, res.getObject(tableName1+"."+cf.getKey()));
                    }
                }
            }
            entitys.add(entity);
        }
        return entitys;
    }
}
