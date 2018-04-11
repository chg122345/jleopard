package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.Config;
import com.leopardframework.core.session.Session;
import com.leopardframework.core.sql.*;
import com.leopardframework.core.util.FieldUtil;
import com.leopardframework.core.util.TableUtil;
import com.leopardframework.exception.NotfoundFieldException;
import com.leopardframework.exception.SessionException;

import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.page.PageInfo;
import com.leopardframework.util.CollectionUtil;

import java.sql.*;
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
 *  核心部分 分别封装对数据库的增删改查
 *   分页查询信息
 *
 */
final class SessionDirectImpl implements Session {

    private static final Log LOG=LogFactory.getLog(SessionDirectImpl.class);
    private Connection conn;
    private PreparedStatement pstm;
    private Statement stm;
    private ResultSet res;
    private final boolean DevModel=Config.getDevModel();


    private void init(){
        this.conn=Config.getConnection();
        if(conn==null){
            LOG.error(" 获取数据库连接失败....");
            throw new SessionException("获取数据库连接失败...");
        }
    }

    public SessionDirectImpl() {
    }

    /**
     *  insert 对象到数据库
     *
     * @param entity  信息封装完成的对象
     * @return     数据库变化的 row 数
     * @throws SQLException
     */
    @Override
    public int Save(Object entity) throws SQLException {
        this.init();
        Sql insertsql =new InsertSql(entity);
        String sql=insertsql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        List values=insertsql.getValues();
        pstm=conn.prepareStatement(sql);
         pstmSetListValues(pstm,values);
        DevModelHelper.outListParameter(DevModel,sql,values);
        return pstm.executeUpdate();
    }

    /**
     *  一次添加多个对象进数据库
     *
     * @param list   把要操作的对象添用一个list封装完整
     * @return     数据库变化的 row 数
     * @throws SQLException
     */
    @Override
    public int SaveMore(List list) throws SQLException {
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
     * @throws SQLException
     */
    @Override
    public int MySql(String sql, Object... args) throws SQLException {
        this.init();
        LOG.info("当前执行的sql语句: \n" +sql);
        pstm=conn.prepareStatement(sql);
        pstmSetArrayValues(pstm,args);
        DevModelHelper.outArrayParameter(DevModel,sql,args);
        return pstm.executeUpdate();
    }

    /**
     *  删除数据库信息操作
     *   把要执行删除的条件封装为一个对象
     *
     * @param entity    封装号信息的对象
     * @return    数据库变化的 row 数
     * @throws SQLException
     */
    @Override
    public int Delete(Object entity) throws SQLException {
        this.init();
        Sql deletesql=new DeleteSql(entity);
        List values=deletesql.getValues();
        String sql=deletesql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        pstm=conn.prepareStatement(sql);
        pstmSetListValues(pstm,values);
        DevModelHelper.outListParameter(DevModel,sql,values);
        return pstm.executeUpdate();
    }

    /**
     *  根据唯一约束主键删除数据
     *
     * @param cls     要删除的对象的类
     * @param primaryKeys    唯一主键( 可传多个值(批量删除))
     * @return    数据库变化的 row 数
     * @throws Exception
     */
    @Override
    public int Delete(Class<?> cls, Object... primaryKeys) throws Exception {
        this.init();
        Sql deletesql=new DeleteSqlMore(cls);
        StringBuilder SQL=new StringBuilder();
        SQL.append(deletesql.getSql()).append(" ").append(DoArrays.getSql(primaryKeys));
        String sql=SQL.toString().toUpperCase();
        LOG.info("当前执行的sql语句: \n" +sql);
        pstm=conn.prepareStatement(sql);
        pstmSetArrayValues(pstm,primaryKeys);
        DevModelHelper.outArrayParameter(DevModel,sql,primaryKeys);
        return pstm.executeUpdate();
    }

   /* @Override
    public int Update(Object entity) throws SQLException {
        this.init();
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
     * @throws SQLException
     */
    @Override
    public int Update(Object entity, Object primaryKey) throws SQLException {
        this.init();
        Sql updatesql=new UpdateSql(entity);
        List pks=FieldUtil.getPrimaryKey(entity.getClass());
        if (CollectionUtil.isEmpty(pks)){
            LOG.error(entity+ " 没有找到唯一标识主键...");
            throw new NotfoundFieldException(entity+ " 没有找到唯一标识主键...");
        }
        StringBuilder SQL=new StringBuilder();
        SQL.append( updatesql.getSql()).append(pks.get(0)).append("=").append("?");
        String sql=SQL.toString().toUpperCase();
        LOG.info("当前执行的sql语句: \n" +sql);
        List values=updatesql.getValues();
        pstm=conn.prepareStatement(sql);
        pstmSetListValues(pstm,values);
        pstm.setObject(values.size()+1,primaryKey);
        DevModelHelper.outParameter(DevModel,sql,primaryKey);
        return pstm.executeUpdate();
    }

    /**
     *  自定义查询sql
     *
     * @param sql    自定义查询sql
     * @param args    动态参数
     * @return           从数据库查询出的结果集
     * @throws Exception
     */
    @Override
    public ResultSet Get(String sql, Object... args) throws Exception {
        this.init();
        pstm=conn.prepareStatement(sql);
        pstmSetArrayValues(pstm,args);
        res=pstm.executeQuery();
        DevModelHelper.outArrayParameter(DevModel,sql,args);
        return res;
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
     * @throws Exception
     */
    @Override
    public List Get(Class<?> cls, String where,Object... args) throws Exception {
        this.init();
        Sql selectsql=new SelectSqlMore(cls);
        StringBuilder SQL=new StringBuilder();
        if(where.startsWith("where")){
            SQL.append( selectsql.getSql()).append("\n").append(where);
        }else{
            SQL.append( selectsql.getSql()).append("\n").append(" where").append(" ").append(where);
        }
        String sql=SQL.toString().toUpperCase();
        LOG.info("当前执行的sql语句: \n" +sql);
        pstm=conn.prepareStatement(sql);
        pstmSetArrayValues(pstm,args);
        res=pstm.executeQuery();
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);

        DevModelHelper.outParameter(DevModel,sql,"");
        return EntityHelper.invoke(res,cls,C_F);
    }

    /**
     * 对查询单个对象的操作    把条件封装为一个完整实体对象
     *
     * @param entity
     * @param <T>    泛型 传一个什么对象 就返回一个什么对象类型
     * @return       查出的单个对象( 如果该对象条件符合多条数据，则只返回第一条数据)
     * @throws Exception
     */
    @Override
    public <T> T Get(T entity) throws Exception{
        this.init();
        Sql selectsql=new SelectSql(entity);
        String sql=selectsql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        List values=selectsql.getValues();
        pstm=conn.prepareStatement(sql);
        pstmSetListValues(pstm,values);
        res=pstm.executeQuery();
        Map<String,String> C_F= FieldUtil.getColumnFieldName(entity.getClass());
        DevModelHelper.outListParameter(DevModel,sql,values);
        List list=EntityHelper.invoke(res,entity.getClass(),C_F);
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
     * @throws Exception
     */
    @Override
    public List Get(Class<?> cls, Object... primaryKeys) throws Exception {
        this.init();
        Sql selectsql=new SelectSqlMore(cls);
        StringBuilder SQL=new StringBuilder();
        List pks=FieldUtil.getPrimaryKey(cls);
        if (CollectionUtil.isEmpty(pks)){
            LOG.error(cls+ " 没有找到唯一标识主键...");
            throw new NotfoundFieldException(cls+ " 没有找到唯一标识主键...");
        }
        SQL.append( selectsql.getSql()).append("\n").append(" where").append(" ")
                .append(pks.get(0)).append(" ").append( DoArrays.getSql(primaryKeys));
        String sql=SQL.toString().toUpperCase();
        LOG.info("当前执行的sql语句: \n" +sql);
        pstm=conn.prepareStatement(sql);
        pstmSetArrayValues(pstm,primaryKeys);
        res=pstm.executeQuery();
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);

        DevModelHelper.outArrayParameter(DevModel,sql,primaryKeys);
        return EntityHelper.invoke(res,cls,C_F);
    }

    /**
     *   从数据库查询所有该类对象 以 list 形式返回
     *
     * @param cls  要查询对象的实体类
     * @return    list
     * @throws Exception
     */
    @Override
    public List Get(Class<?> cls) throws Exception {
        this.init();
        Sql selectsql=new SelectSqlMore(cls);
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);
     //   List entitys=new ArrayList();
        String sql=selectsql.getSql();
        LOG.info("当前执行的sql语句: \n" +sql);
        stm=conn.createStatement();
        res=stm.executeQuery(sql);

        DevModelHelper.outParameter(DevModel,sql,"");
       /* while (res.next()) {
            Object entity=cls.newInstance();
            for (Map.Entry<String, String> cf : C_F.entrySet()) {
                PropertyDescriptor pd = new PropertyDescriptor(cf.getValue(), cls);
                Method write = pd.getWriteMethod();
                write.invoke(entity, res.getObject(cf.getKey()));
            }
            entitys.add(entity);
        }*/
        return EntityHelper.invoke(res,cls,C_F);
    }

    @Override
    public PageInfo Get(Class<?> cls, int page, int pagesize) throws Exception {
        this.init();
        Sql selectsql=new SelectSqlMore(cls);
        Map<String,String> C_F= FieldUtil.getColumnFieldName(cls);
        stm=conn.createStatement();
        String csql="select count(*) from "+TableUtil.getTableName(cls);
        res=stm.executeQuery(csql);
        int total=0;
        while(res.next()){
            total=res.getInt(1);
        }
        System.out.println("总记录数据: "+total);
        PageInfo pm=new PageInfo(total,pagesize);
        StringBuilder SQL=new StringBuilder();
        SQL.append(selectsql.getSql());
        if (page<=1){
            page=1;
        }else if(page>=pm.getTotalPages()){
                page=pm.getTotalPages();
        }
        pm.setPage(page);
        int star=(page-1)*pagesize;
        SQL.append(" ").append("limit").append(" ").append(star).append(",").append(pagesize);
        String sql=SQL.toString().toUpperCase();
        LOG.info("当前执行的sql语句: \n" +sql);
        res=stm.executeQuery(sql);
        DevModelHelper.outParameter(DevModel,sql,page);
        pm.setList(EntityHelper.invoke(res,cls,C_F));
        return pm;
    }

    /**
     *  关闭数据库操作  释放资源
     * @throws SQLException
     */
    @Override
    public void closeSession() throws SQLException {
        if(res!=null){
            res.close();
        }
        if(stm!=null){
            stm.close();
        }
        if(pstm!=null){
            pstm.close();
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
