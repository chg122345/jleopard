package org.jleopard.session;


import java.io.Serializable;
import java.sql.ResultSet;
import java.util.Collection;

import org.jleopard.exception.SqlSessionException;
import org.jleopard.pageHelper.PageInfo;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 * @see  org.jleopard.session.sessionFactory.SessionDirectImpl
 *   管理与数据库操作的方法接口
 */
public interface SqlSession {

    /**
     * -----------------------Save---------------------------------
     **/
    <T> int save(T entity) throws SqlSessionException;

    <T> int saveMore(Collection<T> collection) throws SqlSessionException;

    int mySql(String sql, Object... args) throws SqlSessionException;

    /**
     * -----------------------Delete---------------------------------
     **/
    <T> int delete(T entity) throws SqlSessionException;

    <T> int delete(Class<T> cls, Serializable... primaryKeys) throws SqlSessionException;


    /**
     * -----------------------Update---------------------------------
     **/

    <T> int update(T entity, Serializable... primaryKey) throws SqlSessionException;

    <T> int update(T entity) throws SqlSessionException;

    <T> int updateByWhere(T entity,String where,Object...args) throws SqlSessionException;

    /**
     * -----------------------Get one or one2one m2o---------------------------------
     **/
    ResultSet getBySql(String sql, Object... args) throws SqlSessionException;

    <T> Collection<T> getByJoin(Class<T> cls1, Class<?>[] clazz, String where, Object... args) throws SqlSessionException;

    <T> Collection<T> getByWhere(Class<T> cls, String where, Object... args) throws SqlSessionException;

    <T> Collection<T> get(T entity) throws SqlSessionException;

    <T> Collection<T> getById(Class<T> cls, Serializable... primaryKeys) throws SqlSessionException;

    <T> Collection<T> get(Class<T> cls) throws SqlSessionException;

    <T> PageInfo getToPage(Class<T> cls, int page, int pageSize, String where, Object... args) throws SqlSessionException;

    <T> PageInfo getJoinToPage(Class<T> cls1,Class<?>[] clazz, int page, int pageSize, String where, Object... args) throws SqlSessionException;

    /**
     * -----------------------Commit and rollback---------------------------------
     **/
    void commit()throws SqlSessionException;

    void rollback() throws SqlSessionException;
    /**
     * -----------------------Close---------------------------------
     **/

    void stop() throws SqlSessionException;

    void close() throws SqlSessionException;

}
