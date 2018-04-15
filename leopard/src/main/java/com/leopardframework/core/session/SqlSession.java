package com.leopardframework.core.session;

import com.leopardframework.exception.SqlSessionException;
import com.leopardframework.page.PageInfo;

import java.sql.ResultSet;
import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
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

    <T> List<T> Get(Class<T> cls, String where, Object... args) throws SqlSessionException;

    <T> T Get(T entity) throws SqlSessionException;

    <T> List<T> Get(Class<T> cls, Object... primaryKeys) throws SqlSessionException;

    <T> List<T> Get(Class<T> cls) throws SqlSessionException;

    <T> PageInfo Get(Class<T> cls, int page, int pageSize) throws SqlSessionException;

    //  List Get(String sql,Object... args);

    /**
     * -----------------------Close---------------------------------
     **/

    void Stop() throws SqlSessionException;

    void Close() throws SqlSessionException;

}
