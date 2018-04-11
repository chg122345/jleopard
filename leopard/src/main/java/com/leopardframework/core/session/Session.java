package com.leopardframework.core.session;

import com.leopardframework.page.PageInfo;

import java.sql.ResultSet;
import java.sql.SQLException;
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
 * @see  com.leopardframework.core.session.sessionFactory.SessionImplDirect
 *   管理与数据库操作的方法接口
 */
public interface Session {

    /** -----------------------Save--------------------------------- **/
    int Save(Object entity) throws SQLException;

    int SaveMore(List list) throws SQLException;

    int MySql(String sql,Object... args) throws SQLException;
   /** -----------------------Delete--------------------------------- **/
    int Delete(Object entity) throws SQLException;

    int Delete(Class<?> cls,Object... primaryKeys) throws Exception;


    /** -----------------------Update--------------------------------- **/
  //  int Update(Object entity) throws SQLException;

    int Update(Object entity,Object primaryKey) throws SQLException;

    /** -----------------------Get--------------------------------- **/
    ResultSet Get(String sql, Object... args) throws  Exception;

    List Get(Class<?> cls,String where,Object...args) throws  Exception;

    <T> T Get(T entity) throws  Exception;

    List Get(Class<?> cls,Object... primaryKeys) throws Exception;

    List Get(Class<?> cls) throws  Exception;

    PageInfo Get(Class<?> cls,int page,int pagesize) throws  Exception;

  //  List Get(String sql,Object... args);

    /** -----------------------Close--------------------------------- **/

    void closeSession() throws SQLException;


}
