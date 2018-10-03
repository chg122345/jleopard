package org.jleopard.session.sessionFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.exception.SqlSessionException;
import org.jleopard.session.SqlSession;
import org.jleopard.util.ClassUtil;
import org.jleopard.util.CollectionUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/10
 * <p>
 * Find a way for success and not make excuses for failure.
 * <p>
 * 对查询出的对象赋值
 */
final class EntityHelper {

    /**
     * 查询出的结果赋值给相应对象 不考虑外键值 外键的值为空 不赋值
     *
     * @param res 查出的结果集
     * @param cls 赋值对象类
     * @param C_F
     * @param <T>
     * @return
     * @throws SqlSessionException
     */
    protected static <T> List<T> invoke(ResultSet res, Class<T> cls, Map<String, String> C_F)
            throws SqlSessionException {
        List<T> entitys = new ArrayList<T>();
        String fieldName = null;
        try {
            while (res.next()) {
                T entity = cls.newInstance();
                for (Map.Entry<String, String> cf : C_F.entrySet()) { // 字段名 属性名
                    PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls);
                    Method write = pd.getWriteMethod();
                    Map<String, Class<?>> fns = FieldUtil.getForeignKeys(cls); //外键
                    if (CollectionUtil.isNotEmpty(fns)) {
                        for (Map.Entry<String, Class<?>> fn : fns.entrySet()) {
                            if (cf.getKey().equals(fn.getKey())) {
                                Object fkv = res.getObject(cf.getKey()); // 外键值
                                Class<?> clazz = fn.getValue();
                                Object entity2 = clazz.newInstance();
                                PropertyDescriptor pd2 = new PropertyDescriptor(
                                        FieldUtil.getColumnFieldName(clazz).get(FieldUtil.getPrimaryKeys(clazz).get(0)),
                                        clazz);
                                Method write2 = pd2.getWriteMethod();
                                fieldName = FieldUtil.getPrimaryKeys(clazz).get(0);
                                Object v = ClassUtil.changeType(clazz.getDeclaredField(fieldName), fkv);
                                write2.invoke(entity2, v);
                                write.invoke(entity, entity2); // 外表值设上
                                break;
                            } else if (fns.keySet().contains(cf.getKey())) {
                                continue;
                            } else {
                                fieldName = cf.getValue();
                                write.invoke(entity, ClassUtil.changeType(cls.getDeclaredField(fieldName), res.getObject(cf.getKey())));
                            }
                        }
                    } else {
                        fieldName = cf.getValue();
                        write.invoke(entity, ClassUtil.changeType(cls.getDeclaredField(fieldName), res.getObject(cf.getKey())));
                    }
                }
                entitys.add(entity);
            }
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... .", e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... ", e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... ", e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... ", e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException("反射调用方法或构造方法失败... ", e);
        } catch (NoSuchFieldException e) {
            throw new SqlSessionException("没有找到字段[" + cls + "." + fieldName + "]", e);
        }
        return entitys;
    }

    /**
     * 查询出的结果赋值给相应对象 (注 : 只赋值给有外键的对象) 返回的是一个完整数据的对象 ..
     *
     * @param res  数据库查出的结果集
     * @param cls1 带外键的对象类
     * @param cls2 属于对象一的外键类
     * @param <T>
     * @return
     * @throws SqlSessionException
     */
    protected static <T> List<T> invoke(ResultSet res, Class<T> cls1, Class<?> cls2) throws SqlSessionException {
        List<T> entitys = new ArrayList<T>();
        String tableName1 = TableUtil.getTableName(cls1);
        String tableName2 = TableUtil.getTableName(cls2);
        try {
            while (res.next()) {
                T entity = cls1.newInstance();
                for (Map.Entry<String, String> cf : FieldUtil.getColumnFieldName(cls1).entrySet()) {
                    PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls1);
                    Method write = pd.getWriteMethod();
                    List<String> fns = FieldUtil.getForeignKeyName(cls1);
                    for (String fn : fns) {
                        if (cf.getKey().equals(fn)) {
                            Object entity2 = cls2.newInstance();
                            for (Map.Entry<String, String> cf2 : FieldUtil.getColumnFieldName(cls2).entrySet()) {
                                PropertyDescriptor pd2 = new PropertyDescriptor(cf2.getValue(), cls2);
                                Method write2 = pd2.getWriteMethod();
                                write2.invoke(entity2, ClassUtil.changeType(cls1.getDeclaredField(cf.getValue()), res.getObject(tableName2 + "." + cf2.getKey())));
                            }
                            write.invoke(entity, entity2); // 外表值设上
                        } else {
                            write.invoke(entity, ClassUtil.changeType(cls1.getDeclaredField(cf.getValue()), res.getObject(tableName1 + "." + cf.getKey())));
                        }
                    }
                }
                entitys.add(entity);
            }
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... .", e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... ", e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... ", e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... ", e);
        } catch (InvocationTargetException | NoSuchFieldException e) {
            throw new SqlSessionException("反射调用方法或构造方法失败... ", e);
        }
        return entitys;
    }

    /**
     * @param res   数据库查出的结果集
     * @param cls1  带外键的对象类(目标对象)
     * @param clazz 属于目标对象的外键类
     * @return
     * @throws SqlSessionException
     */
    protected static <T> List<T> invoke(ResultSet res, SqlSession session, Class<T> cls1, Class<?>[] clazz)
            throws SqlSessionException {
        List<T> entitys = new ArrayList<T>();
        String tableName1 = TableUtil.getTableName(cls1);
        String fieldName = null;
        try {
            while (res.next()) {
                T entity = cls1.newInstance();
                String idColName = null;
                for (Map.Entry<String, String> cf : FieldUtil.getColumnFieldName(cls1).entrySet()) {
                    if (cf.getValue().equals(FieldUtil.getPrimaryKeys(cls1).get(0))) {
                        idColName = cf.getKey();
                    }
                    PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls1);
                    Method write = pd.getWriteMethod();
                    Map<String, Class<?>> ftMap = FieldUtil.getForeignKeys(cls1);
                    for (Class<?> cls2 : clazz) {

                        for (Map.Entry<String, Class<?>> ft : ftMap.entrySet()) { // 外键 外键类
                            if (cls2 == ft.getValue()) {
                                String tableName2 = TableUtil.getTableName(cls2);
                                if (cf.getKey().equals(ft.getKey())) {   // 目标对象类的字段名 == 此外键名
                                    Object entity2 = cls2.newInstance();
                                    for (Map.Entry<String, String> cf2 : FieldUtil.getColumnFieldName(cls2)
                                            .entrySet()) {
                                        PropertyDescriptor pd2 = new PropertyDescriptor(cf2.getValue(), cls2);
                                        Method write2 = pd2.getWriteMethod();
                                        fieldName = cf2.getValue();
                                        try {
                                            write2.invoke(entity2, ClassUtil.changeType(cls2.getDeclaredField(fieldName), res.getObject(tableName2 + "." + cf2.getKey())));
                                        } catch (NoSuchFieldException e) {
                                            throw new SqlSessionException("没有找到字段[" + cls2 + "." + fieldName + "]", e);
                                        }
                                    }
                                    write.invoke(entity, entity2); // 外表值设上
                                } else if (ftMap.keySet().contains(cf.getKey())) {
                                    continue;
                                } else {
                                    fieldName = cf.getValue();
                                    write.invoke(entity, ClassUtil.changeType(cls1.getDeclaredField(fieldName), res.getObject(tableName1 + "." + cf.getKey())));
                                }
                            }
                        }
                    }
                }
                for (Map.Entry<String, Class<?>> jct : FieldUtil.getJoinColumnAndType(cls1).entrySet()) {
                    PropertyDescriptor pd = new PropertyDescriptor(jct.getKey(), cls1);
                    Method write = pd.getWriteMethod();
                    // 处理一对多关系字段
                    for (Map.Entry<String, Class<?>> jft : FieldUtil.getJoinAndForeignKeys(cls1).entrySet()) { // 是一对多关系
                        if (jct.getValue() == jft.getValue()) {
                            Collection<?> list = session.getByWhere(jft.getValue(), jft.getKey() + " = ?", (Serializable) res.getObject(tableName1 + "." + idColName));
                            if (CollectionUtil.isNotEmpty(list)) {
                                write.invoke(entity, list);
                            } else {
                                continue;
                            }
                        }
                    }
                }
                entitys.add(entity);
            }
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... .", e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... ", e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... ", e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... ", e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException("反射调用方法或构造方法失败... ", e);
        } catch (NoSuchFieldException e) {
            throw new SqlSessionException("没有找到字段[" + cls1 + "." + fieldName + "]", e);
        }
        return entitys;
    }
}
