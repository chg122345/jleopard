package com.leopardframework.core.session.sessionFactory;

import com.leopardframework.core.Config;
import com.leopardframework.core.SqlBuilder;
import com.leopardframework.core.get.PrimaryKeyName;
import com.leopardframework.core.session.Session;
import com.leopardframework.exception.SessionException;
import com.leopardframework.page.PageInfo;
import com.leopardframework.util.ArrayUtil;
import com.leopardframework.util.CollectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 * @see DoArrays
 * @   会话的直接实现类 操作数据库，进行数据操作
 *                          并返回对应的对象
 */
class SessionImplDirect implements Session {

    private Connection conn;
    private PreparedStatement pstm;
    private ResultSet res;
    private final boolean DevModel=Config.getDevModel();

    /*public SessionImplDirect(Connection connection){
        this.conn=connection;
    }*/
    private void init(){
        this.conn=Config.getConnection();
        if(conn==null){
            throw new SessionException("获取数据库连接失败...");
        }
    }

    public SessionImplDirect() {
    }

    @Override
    public int Save(Object object) throws SQLException {
        this.init();
        Map<String,List<Object>> sqlValues=SqlBuilder.getSaveSqlValues(object);
        String sql=null;
        List<Object> values=null;
        for (Map.Entry<String,List<Object>> sv :sqlValues.entrySet() ){
                        sql=sv.getKey();
                        values=sv.getValue();
        }
          pstm=conn.prepareStatement(sql);

        for (int i=0;i<values.size();++i){
            pstm.setObject(i+1, values.get(i));
        }
            if(DevModel){
                 System.out.println(sql);
                 System.out.print("Parameters : " );
                Iterator it = values.iterator();
                while (it.hasNext()){
                    System.out.print(it.next()+" ");
                }
                System.out.println();
             }
        return  pstm.executeUpdate();
    }

    @Override
    public int SaveMore(List list) throws SQLException {
        if(CollectionUtil.isEmpty(list)){
            throw new SessionException("Save(List<Object> list) 参数不允许为空列表");
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

    @Override
    public int MySql(String sql, Object... args) throws SQLException {
          this.init();
        pstm=conn.prepareStatement(sql);
        for(int i=0;i<args.length;++i){
            pstm.setObject(i+1,args[i]);
        }
        if(DevModel){
            System.out.println(sql);
            System.out.print("Parameters : " );
            for(int i=0;i<args.length;++i){
                System.out.print(args[i]+"  ");
            }
            System.out.println();
        }
        return pstm.executeUpdate();
    }

    @Override
    public int Delete(Object object) throws SQLException {
            this.init();
        Map<String,List<Object>> sqlValues=SqlBuilder.getDeleteSqlValues(object);
        String sql=null;
        List<Object> values=null;
        for (Map.Entry<String,List<Object>> sv :sqlValues.entrySet() ){
            sql=sv.getKey();
            values=sv.getValue();
        }
        pstm=conn.prepareStatement(sql);
        for (int i=0;i<values.size();++i){
            pstm.setObject(i+1, values.get(i));
        }
        if(DevModel){
            System.out.println(sql);
            System.out.print("Parameters :" );
            Iterator it = values.iterator();
            while (it.hasNext()){
                System.out.print(it.next()+" ");
            }
            System.out.println();
        }
        return  pstm.executeUpdate();
    }

    @Override
    public int Delete(Class<?> cls, Object... primaryKeys) throws Exception {
        if(ArrayUtil.isEmpty(primaryKeys)){
            throw new SessionException(" Delete(Class<?> cls, Object... primaryKeys) 参数值不允许为空...");
        }
        this.init();
        StringBuilder SQL=new StringBuilder();
        String sql=null;
        SQL.append(SqlBuilder.getDeleteSql(cls.newInstance())).append(" \n").append(" where").append(" ")
                .append(PrimaryKeyName.getPrimaryKeyName(cls)).append(" ");
           String dosql=DoArrays.getSql(primaryKeys);
                sql=SQL.append(dosql).toString();
                pstm=conn.prepareStatement(sql);
                for(int i=0;i<primaryKeys.length;++i){
                    pstm.setObject(i+1,primaryKeys[i]);
                }
           /* if(primaryKeys.length>1){
                SQL.append("in").append("(");
                for(int i=0;i<primaryKeys.length;++i){
                    SQL.append("?").append(",");
                }
                SQL.deleteCharAt(SQL.length()-1).append(")");
                sql=SQL.toString();
                pstm=conn.prepareStatement(sql);
                for(int i=0;i<primaryKeys.length;++i){
                    pstm.setObject(i+1,primaryKeys[i]);
                }
            }else{
                SQL.append("=").append(primaryKeys[0]);
                sql=SQL.toString();
                pstm=conn.prepareStatement(sql);
            }*/

            if(DevModel){
                System.out.println(sql);
                System.out.print("Parameters :" );
                for(int i=0;i<primaryKeys.length;++i){
                    System.out.print(primaryKeys[i]+" ");
                }
                System.out.println();
            }

        return pstm.executeUpdate();
    }


  /*  @Override
    public int Update(Object object) {
        return 0;
    }*/

    @Override
    public int Update(Object object, Object primaryKey) {
        return 0;
    }

    @Override
    public  ResultSet Get(String sql, Object... args) {
        return null;
    }

    @Override
    public List Get(Class<?> cls, String where, Object... args) throws Exception {
        return null;
    }

    @Override
    public Object Get(Object object) {
        return null;
    }

    @Override
    public  List Get(Class<?> cls, Object... primaryKeys) {
        return null;
    }

    @Override
    public List<Object> Get(Class<?> cls) {
        return null;
    }

    @Override
    public PageInfo Get(Class<?> cls, int page, int pagesize) throws Exception {
        return null;
    }

    @Override
    public void closeSession() throws SQLException {
        if(res!=null){
            res.close();
        }
        if(pstm!=null){
            pstm.close();
        }
       /* if (conn!=null){
            conn.close();
        }*/

    }
}
