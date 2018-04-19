package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.session.SqlSession;
import com.leopardframework.core.sql.*;
import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.exception.NotFoundFieldException;
import com.leopardframework.exception.SessionException;
import com.leopardframework.exception.SqlSessionException;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.page.PageInfo;
import com.leopardframework.util.ArrayUtil;
import com.leopardframework.util.ClassUtil;
import com.leopardframework.util.CollectionUtil;
import com.leopardframework.util.StringUtil;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/10
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *  核心部分 分别封装对数据库的增删改查
 *   分页查询信息
 *
 */
final class SessionDirectImpl implements SqlSession {

    private static final Log LOG=LogFactory.getLog(SessionDirectImpl.class);
    private Connection conn;
    private PreparedStatement pstm;
    private Statement stm;
    private ResultSet res;
    private final boolean DevModel=SessionFactory.Config.getDevModel();


    private void open(){
        if(conn==null){
            LOG.error(" 获取数据库连接失败....");
            throw new SessionException("获取数据库连接失败... or session 已经关闭...");
        }else{
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                throw new SessionException("设置事物出错了..."+e);
            }
        }
    }

    /**
     * 初始化时建立实体对象对应的数据库表
     *    当前表名在数据库中已存在时 不会新建表
     */
    public SessionDirectImpl(String packagePath) {
        this.conn=SessionFactory.Config.getConnection();
        Set<Class<?>> set=ClassUtil.getClassSetByPackagename(packagePath);
        List<String> list=TableUtil.showAllTableName(conn);   //ArraysHelper.toUpperCase(TableUtil.showAllTableName(conn));
        //System.out.println("对比："+ArraysHelper.toUpperCase(list));
        if(CollectionUtil.isEmpty(set)){
            LOG.error("获取到的实体类为空...");
            throw new SessionException("获取到的实体类为空...");
        }
        for (Class<?> cls : set) {
            if (CollectionUtil.isEmpty(list)) {
                continue;
            } else {
                if (list.contains(TableUtil.getTableName(cls))) {
                    continue;
                }
                Sql create = new CreateTableSql(cls);
                String sql = create.getSql();
                try {
                    stm = conn.createStatement();
                    stm.executeUpdate(sql);
                } catch (SQLException e) {
                    LOG.error(cls + "创建表时出错...", e);
                    e.printStackTrace();
                }
                DevModelHelper.outParameter(DevModel, sql, "");
            }
        }
    }

    /**
     *  insert 对象到数据库
     *
     * @param entity  信息封装完成的对象
     * @return     数据库变化的 row 数
     * @throws SqlSessionException
     */
    @Override
    public <T> int Save(T entity) throws SqlSessionException {
        this.open();
        Sql insertsql =new InsertSql(entity);
        String sql=insertsql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        List values=insertsql.getValues();
        try {
            pstm=conn.prepareStatement(sql);
            pstmSetListValues(pstm,values);
            DevModelHelper.outListParameter(DevModel,sql,values);
            return pstm.executeUpdate();
        } catch (SQLException e) {
           throw new SqlSessionException(" sql执行出错了..."+e);
        }
    }

    /**
     *  一次添加多个对象进数据库
     *
     * @param list   把要操作的对象添用一个list封装完整
     * @return     数据库变化的 row 数
     * @throws SqlSessionException
     */
    @Override
    public <T> int SaveMore(List<T> list) throws SqlSessionException {
        if(CollectionUtil.isEmpty(list)){
          /*  throw new SessionException("Save(List<Object> list) 参数不允许为空列表");*/
            LOG.warn(" 传入的 list 对象为空...");
            return 0;
        }
        int result=0;
        for (Object object:list){
            int temp=this.Save(object);
            if(temp>0){
                result++;
            }
        }
        return result;
    }

    /**
     *  执行自定义sql ( 注 :  不能是查询语句 不能是查询语句 不能是查询语句)
     *   (此处只能是 更新 操作的sql语句...)
     *
     * @param sql   动态sql  例: select * from user
     *                              where id=? and name=?
     * @param args      动态sql的参数  一定要按顺序传递
     * @return  数据库变化的 row 数
     * @throws SqlSessionException
     */
    @Override
    public int MySql(String sql, Object... args) throws SqlSessionException {
        this.open();
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            pstm = conn.prepareStatement(sql);
            if (ArrayUtil.isNotEmpty(args)) {
                pstmSetArrayValues(pstm, args);
            }
            DevModelHelper.outArrayParameter(DevModel, sql, args);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了..."+e);
        }
    }

    /**
     *  删除数据库信息操作
     *   把要执行删除的条件封装为一个对象
     *
     * @param entity    封装号信息的对象
     * @return    数据库变化的 row 数
     * @throws SqlSessionException
     */
    @Override
    public <T> int Delete(T entity) throws SqlSessionException {
        this.open();
        Sql deletesql=new DeleteSql(entity);
        List values=deletesql.getValues();
        String sql=deletesql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            pstm = conn.prepareStatement(sql);
            pstmSetListValues(pstm, values);
            DevModelHelper.outListParameter(DevModel, sql, values);
            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        }
    }

    /**
     *  根据唯一约束主键删除数据
     *
     * @param cls     要删除的对象的类
     * @param primaryKeys    唯一主键( 可传多个值(批量删除))
     * @return    数据库变化的 row 数
     * @throws SqlSessionException
     */
    @Override
    public <T> int Delete(Class<T> cls, Object... primaryKeys) throws SqlSessionException {
        this.open();
        Sql deletesql=new DeleteSqlMore(cls);
        StringBuilder SQL=new StringBuilder();
        SQL.append(deletesql.getSql()).append(" ").append(ArraysHelper.getSql(primaryKeys));
        String sql=SQL.toString()/*.toUpperCase()*/;
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            pstm=conn.prepareStatement(sql);
            if(ArrayUtil.isNotEmpty(primaryKeys)){
             pstmSetArrayValues(pstm,primaryKeys);
            }

        DevModelHelper.outArrayParameter(DevModel,sql,primaryKeys);

            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        }
    }

   /* @Override
    public int Update(Object entity) throws SQLException {
        this.open();
        Sql updatesql=new UpdateSql(entity);
        String sql=updatesql.getSql();
        List values=updatesql.getValues();
        LOG.info("当前执行的sql语句: \n" +sql);
        pstm=conn.prepareStatement(sql);
        pstmSetListValues(pstm,values);
        outListParameter(sql,values);
        return pstm.executeUpdate();
    }*/

    /**
     *  根据唯一主键更新数据
     *     目标数据封装进完整对象 ( 主键可有可无, 此处不在该对象中取主键值(即主键默认不可更改))
     *
     * @param entity   封装好的对象
     * @param primaryKey   唯一主键值
     * @return   数据库变化的 row 数
     * @throws SqlSessionException
     */
    @Override
    public <T> int Update(T entity, Object primaryKey) throws SqlSessionException {
        this.open();
        Sql updatesql=new UpdateSql(entity);
        List pks=FieldUtil.getPrimaryKeys(entity.getClass());
        if (CollectionUtil.isEmpty(pks)){
            LOG.error(entity+ " 没有找到唯一标识主键...");
            throw new NotFoundFieldException(entity+ " 没有找到唯一标识主键...");
        }
        StringBuilder SQL=new StringBuilder();
        SQL.append( updatesql.getSql()).append(pks.get(0)).append("=").append("?");
        String sql=SQL.toString()/*.toUpperCase()*/;
        LOG.info("当前执行的sql语句: \n" +sql);
        List values=updatesql.getValues();
        try {
            pstm = conn.prepareStatement(sql);
            pstmSetListValues(pstm, values);
            pstm.setObject(values.size() + 1, primaryKey);
            values.add(primaryKey);
            DevModelHelper.outParameter(DevModel, sql, values);

            return pstm.executeUpdate();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        }
    }

    /**
     *  自定义查询sql
     *
     * @param sql    自定义查询sql
     * @param args    动态参数
     * @return           从数据库查询出的结果集
     * @throws SqlSessionException
     */
    @Override
    public ResultSet Get(String sql, Object... args) throws SqlSessionException {
        this.open();
        try {
            pstm = conn.prepareStatement(sql);
            if(ArrayUtil.isNotEmpty(args)){
                pstmSetArrayValues(pstm, args);
            }
            res = pstm.executeQuery();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        }
        DevModelHelper.outArrayParameter(DevModel, sql, args);
        return res;
    }

    /**
     *   多表链接查询 返回有外键的对象  m 2 one 返回m 对象
     *     one对象放置m 对象中
     * @param cls1  m对象类  （返回的对象类）
     * @param cls2  one对象类
     * @param where   查询条件 不加条件留空 "" 后面参数也必须留空 ""
     * @param args    查询条件的动态参数 按顺序依次写入
     * @param <T>
     * @return
     * @throws SqlSessionException
     */
    @Override
    public <T> List<T> Get(Class<T> cls1, Class<?> cls2, String where, Object... args) throws SqlSessionException {
        this.open();
        Sql joinSql=new JoinSql(cls1,cls2);
        StringBuilder SQL=new StringBuilder();
        if (StringUtil.isEmpty(where)) {
            SQL.append(joinSql.getSql());
        } else {
            if (where.startsWith("where")) {
                SQL.append(joinSql.getSql()).append("\n").append(where);
            } else {
                SQL.append(joinSql.getSql()).append("\n").append(" where").append(" ").append(where);
            }
        }
        String sql=SQL.toString();
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            pstm=conn.prepareStatement(sql);
            if(ArrayUtil.isNotEmpty(args)){
              //  System.out.println("pstm 设置" +args);
                pstmSetArrayValues(pstm,args);
            }
            res=pstm.executeQuery();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了...."+e);
        }
        DevModelHelper.outArrayParameter(DevModel,sql,args);
        try {
            return EntityHelper.invoke(res,cls1,cls2);
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... ."+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException("反射调用方法或构造方法失败... "+e);
        }

    }

    /**
     *   查询指定对象类  自定义查询条件 动态sql
     *   拼装后的sql 例:  SELECT ID,NAME,PHONE,ADDRESS FROM USER
     *           WHERE ID>？ AND ID<？ ORDER BY ID DESC
     *              按顺序传参
     *
     * @param cls   查询指定对象类
     * @param where
     *             sql 后续条件: (where(可有可无,处理时默认去掉自带where关键字)) id>10086 and id<10089 order by id desc
     * @return     查出的结果 list 形式
     * @throws SqlSessionException
     */
    @Override
    public <T>List<T> Get(Class<T> cls, String where,Object... args) throws SqlSessionException {
        this.open();
        Sql selectsql=new SelectSqlMore(cls);
        StringBuilder SQL=new StringBuilder();
        if(where.startsWith("where")){
            SQL.append( selectsql.getSql()).append("\n").append(where);
        }else{
            SQL.append( selectsql.getSql()).append("\n").append(" where").append(" ").append(where);
        }
        String sql=SQL.toString()/*.toUpperCase()*/;
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            pstm=conn.prepareStatement(sql);
            if (ArrayUtil.isNotEmpty(args)) {
                pstmSetArrayValues(pstm, args);
            }
            res=pstm.executeQuery();
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        }
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);

        DevModelHelper.outParameter(DevModel,sql,"");
        try {
            return EntityHelper.invoke(res,cls,C_F);
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... "+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException("反射调用方法或构造方法失败... "+e);
        }
    }

    /**
     * 对查询单个对象的操作    把条件封装为一个完整实体对象
     *
     * @param entity
     * @param <T>    泛型 传一个什么对象 就返回一个什么对象类型
     * @return       查出的单个对象( 如果该对象条件符合多条数据，则只返回第一条数据)
     * @throws SqlSessionException
     */
    @Override
    public <T> T Get(T entity) throws SqlSessionException {
        this.open();
        Sql selectsql=new SelectSql(entity);
        String sql=selectsql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        List<Object> values=selectsql.getValues();
        try {
            pstm=conn.prepareStatement(sql);
            pstmSetListValues(pstm,values);
            res=pstm.executeQuery();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        }

        Map<String,String> C_F= FieldUtil.getColumnFieldName(entity.getClass());
        DevModelHelper.outListParameter(DevModel,sql,values);
        List list= null;
        try {
            list = EntityHelper.invoke(res,entity.getClass(),C_F);
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... "+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException("反射调用方法或构造方法失败... "+e);
        }
        if(CollectionUtil.isEmpty(list)){
            return null;
        }else{
            return (T) list.get(0);
        }

    }

    /**
     *   根据唯一主键查询  返回一个或多个对象(由你带的参数决定 1 or m)
     *
     * @param cls   要返回的对象类
     * @param primaryKeys    该对象的唯一主键（可传多个 查询多个）
     *                       注: 如果该对象有多个主键，则默认取第一主键作为此次查询的标识
     * @return
     * @throws SqlSessionException
     */
    @Override
    public <T> List<T> Get(Class<T> cls, Object... primaryKeys) throws SqlSessionException {
        this.open();
        Sql selectsql=new SelectSqlMore(cls);
        StringBuilder SQL=new StringBuilder();
        List pks=FieldUtil.getPrimaryKeys(cls);
        if (CollectionUtil.isEmpty(pks)){
            LOG.error(cls+ " 没有找到唯一标识主键...");
            throw new NotFoundFieldException(cls+ " 没有找到唯一标识主键...");
        }
        SQL.append( selectsql.getSql()).append("\n").append(" where").append(" ")
                .append(pks.get(0)).append(" ").append( ArraysHelper.getSql(primaryKeys));
        String sql=SQL.toString()/*.toUpperCase()*/;
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            pstm = conn.prepareStatement(sql);
            if (ArrayUtil.isNotEmpty(primaryKeys)) {
                pstmSetArrayValues(pstm, primaryKeys);
            }
            res = pstm.executeQuery();
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了..."+e);
        }
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);

        DevModelHelper.outArrayParameter(DevModel,sql,primaryKeys);
        try {
            return EntityHelper.invoke(res,cls,C_F);
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... "+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException("sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException(" 反射调用方法或构造方法失败... "+e);
        }
    }

    /**
     *   从数据库查询所有该类对象 以 list 形式返回
     *
     * @param cls  要查询对象的实体类
     * @return    list
     * @throws SessionException
     */
    @Override
    public <T> List<T> Get(Class<T> cls) throws SqlSessionException {
        this.open();
        Sql selectsql=new SelectSqlMore(cls);
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);
     //   List entitys=new ArrayList();
        String sql=selectsql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        try {
            stm = conn.createStatement();
            res = stm.executeQuery(sql);
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        }

        DevModelHelper.outParameter(DevModel,sql,"");
        try {
            return EntityHelper.invoke(res,cls,C_F);
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败... "+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException(" 反射调用方法或构造方法失败... "+e);
        }
    }

    /**
     *  主流的分页查询 (仅支持mysql)
     *
     * @param cls  要查询的对象的实体类
     * @param page     查询第几页 start 1
     * @param pageSize   每页显示多少条数据
     * @return      PageInfo类(分页信息)
     * @throws Exception
     */
    @Override
    public <T> PageInfo Get(Class<T> cls, int page, int pageSize) throws SqlSessionException {
        this.open();
        Sql selectsql=new SelectSqlMore(cls);
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);
        try {
            stm=conn.createStatement();

        String csql="select count(*) from "+TableUtil.getTableName(cls);
        res=stm.executeQuery(csql);
        int total=0;
        while(res.next()){
            total=res.getInt(1);
        }

      //  System.out.println("总记录数据: "+total);
        PageInfo pm=new PageInfo(total,pageSize);
        StringBuilder SQL=new StringBuilder();
        SQL.append(selectsql.getSql());
        if (page<=1){
            page=1;
        }else if(page>=pm.getTotalPages()){
                page=pm.getTotalPages();
        }
        pm.setPage(page);
         //   System.out.println("page数: "+page);
        int star=(page-1)*pageSize;
        SQL.append(" ").append("limit").append(" ").append(star).append(",").append(pageSize);
        String sql=SQL.toString()/*.toUpperCase()*/;
        LOG.info("当前执行的sql语句: \n" +sql);
        res=stm.executeQuery(sql);
        DevModelHelper.outParameter(DevModel,sql,page);
        pm.setList(EntityHelper.invoke(res,cls,C_F));
        return pm;
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败.... "+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException(" 反射调用方法或构造方法失败... "+e);
        }
    }

    @Override
    public <T> PageInfo Get(Class<T> cls1, Class<?> cls2, int page, int pageSize) throws SqlSessionException {
        this.open();
        Sql joinSql=new JoinSql(cls1,cls2);
        try {
            stm=conn.createStatement();
            String csql="select count(*) from "+TableUtil.getTableName(cls1);
            res=stm.executeQuery(csql);
            int total=0;
            while(res.next()){
                total=res.getInt(1);
            }
            PageInfo pageInfo=new PageInfo(total,pageSize);
            StringBuilder SQL=new StringBuilder();
            if (page<=1){
                page=1;
            }else if(page>=pageInfo.getTotalPages()){
                page=pageInfo.getTotalPages();
            }
            pageInfo.setPage(page);
            int star=(page-1)*pageSize;
            SQL.append(joinSql.getSql());
            SQL.append(" ").append("limit").append(" ").append(star).append(",").append(pageSize);
            String sql=SQL.toString();
            LOG.info("当前执行的sql语句: \n" +sql);
            res=stm.executeQuery(sql);
            DevModelHelper.outParameter(DevModel,sql,page);
            pageInfo.setList(EntityHelper.invoke(res,cls1,cls2));
            return pageInfo;
        } catch (IllegalAccessException e) {
            throw new SqlSessionException("反射调用private属性设值失败.."+e);
        } catch (InstantiationException e) {
            throw new SqlSessionException("反射调用实例化失败... "+e);
        } catch (IntrospectionException e) {
            throw new SqlSessionException("反射调用构造方法失败... "+e);
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        } catch (InvocationTargetException e) {
            throw new SqlSessionException(" 反射调用方法或构造方法失败... "+e);
        }
    }

    @Override
    public void Commit() throws SqlSessionException {
        try {
            conn.commit();
        } catch (SQLException e) {
           throw new SqlSessionException("事务提交出错了..."+e);
        }
    }

    @Override
    public void Rollback() throws SqlSessionException {
        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new SqlSessionException("事物回滚出错了..."+e);
        }
    }

    /**
     *  关闭数据库操作  释放资源
     * @throws SqlSessionException
     */
    @Override
    public void Stop() throws SqlSessionException {
        try {
            if (res != null) {
                res.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        }

    }

    @Override
    public void Close() throws SqlSessionException {

        try {
            if (res != null) {
                res.close();
            }
            if (stm != null) {
                stm.close();
            }
            if (pstm != null) {
                pstm.close();
            }
            if (conn != null) {
                conn.close();
                this.conn = null;
            }
        } catch (SQLException e) {
            throw new SqlSessionException(" sql执行出错了... "+e);
        }
    }

    /**------------------------处理动态参数的方法--------------------------------------------**/
    private void pstmSetListValues(PreparedStatement pstm,List values) throws SQLException {
        for(int i=0;i<values.size();++i){
            pstm.setObject(i+1,values.get(i));
        }
    }
    private void pstmSetArrayValues(PreparedStatement pstm,Object[] args) throws SQLException {
        for(int i=0;i<args.length;++i){
            pstm.setObject(i+1,args[i]);
        }
    }
}
