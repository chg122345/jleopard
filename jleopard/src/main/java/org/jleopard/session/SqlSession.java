package org.jleopard.session;


import java.sql.ResultSet;
import java.util.List;

import org.jleopard.exception.SqlSessionException;
import org.jleopard.pageHelper.PageInfo;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 * @see  com.leopardframework.core.session.sessionFactory.SessionDirectImpl
 *   管理与数据库操作的方法接口
 */
public interface SqlSession {

    /**
     * -----------------------Save---------------------------------
     **/
    <T> int Save(T entity) throws SqlSessionException;

    <T> int SaveMore(List<T> list) throws SqlSessionException;

    int MySql(String sql, Object... args) throws SqlSessionException;

    /**
     * -----------------------Delete---------------------------------
     **/
    <T> int Delete(T entity) throws SqlSessionException;

    <T> int Delete(Class<T> cls, Object... primaryKeys) throws SqlSessionException;


    /**
     * -----------------------Update---------------------------------
     **/
    //  int Update(Object entity) throws SQLException;

    <T> int Update(T entity, Object primaryKey) throws SqlSessionException;

    /**
     * -----------------------Get---------------------------------
     **/
    ResultSet Get(String sql, Object... args) throws SqlSessionException;

    <T> List<T> Get(Class<T> cls1, Class<?> cls2, String where, Object... args) throws SqlSessionException;

    <T> List<T> Get(Class<T> cls, String where, Object... args) throws SqlSessionException;

    <T> T Get(T entity) throws SqlSessionException;

    <T> List<T> Get(Class<T> cls, Object... primaryKeys) throws SqlSessionException;

    <T> List<T> Get(Class<T> cls) throws SqlSessionException;

    <T> PageInfo Get(Class<T> cls, int page, int pageSize) throws SqlSessionException;

    <T> PageInfo Get(Class<T> cls1,Class<?>cls2 ,int page, int pageSize) throws SqlSessionException;

    //  List Get(String sql,Object... args);
    /**
     * -----------------------Commit and rollback---------------------------------
     **/
    void Commit()throws SqlSessionException;

    void Rollback() throws SqlSessionException;
    /**
     * -----------------------Close---------------------------------
     **/

    void Stop() throws SqlSessionException;

    void Close() throws SqlSessionException;

}
