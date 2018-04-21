package com.leopard.test;

import com.leopardframework.core.Factory;
import com.leopardframework.core.annotation.Column;
import com.leopardframework.core.annotation.Table;
import com.leopardframework.core.enums.Primary;
import com.leopardframework.core.session.SqlSession;
import com.leopardframework.core.session.sessionFactory.SessionFactory;
import com.leopardframework.core.sql.*;
import com.leopardframework.exception.SqlSessionException;
import com.leopardframework.generator.GeneratorFactory;
import com.leopardframework.loadxml.XmlFactoryBuilder;
import com.leopardframework.page.PageInfo;
import com.leopardframework.plugins.DBPlugin;
import com.leopardframework.test.entity.Article;
import com.leopardframework.test.entity.Student;
import com.leopardframework.test.entity.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class UserTest {

    @Test
    public void TTTT(){
        try {
           Class<?> cls= Class.forName("com.leopard.entity.User");
            Table table=cls.getAnnotation(Table.class);
            String tableName=table.value();
            if(tableName==null||"".equals(tableName)){
                tableName=cls.getSimpleName();
            }
            System.out.println(tableName);
            String columnName=null;
            Primary pk=null;
            boolean un;
            Field[] fields =cls.getDeclaredFields();
            for(Field field:fields){
                System.out.println(field.getName()+" 变量类型： "+field.getType());
                System.out.println(field.getType() == long.class);
                Column column=field.getDeclaredAnnotation(Column.class);
                columnName=column.value().toUpperCase();
                pk=column.isPrimary();
                if(columnName==null||"".equals(columnName)){
                    columnName=field.getName().toUpperCase();
                }if(pk==Primary.NO){
                      //  continue;
                }
              //  System.out.println(field.getType()==Long.class);
               // System.out.println(columnName+"   "+ pk + " "+un);
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* @Test
   public void sqlTest(){
        User user=new User();
        user.setId(111);
        user.setName("AAAAA");
        user.setPhone("15770549440");
       // SqlBuilder SS=new SqlBuilder();
       // Map<String,List<Object>> sqlv= SqlBuilder.getSaveSqlValues(user);
        List<Object> values=null;
        for (Map.Entry<String,List<Object>> sql : sqlv.entrySet()) {
            System.out.println(sql.getKey());
            values = sql.getValue();
        }
        for(int i=0;i<values.size();++i){
            System.out.println(values.get(i)+"  "+ (i+1));
        }
     //   String sql=sqlv.
       // System.out.println(sql);
    }*/

    /*@Test
    public void SaveUserTest(){
        SqlSession session=SessionFactory.openSession();
        User u=new User();
        u.setId(6);
        u.setName("leopard");
        u.setPhone("15770549440");
        u.setAddress("China");
        try {
            int i=session.Save(u);
            System.out.println(" Success "+i);
            session.closeSession();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void SaveUserTest2(){
        SqlSession session=SessionFactory.openSession();
        List<User> users=new ArrayList<>();
        for(int id=10;id<20;id++){
            users.add(new User(id,"leopard","15770549440"));
        }
        int temp;
        try {
            temp=session.SaveMore(users);
            System.out.println(temp);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void getPK(){
        SqlSession session=SessionFactory.openSession();
        try {
           int temp=session.Delete(User.class,1,2,3,4,5,6,10085);
           System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(FieldUtil.getPrimaryKeys(User.class).get(0));
    }

    @Test
    public void Delobj(){
        SqlSession session=SessionFactory.openSession();
        User user=new User();
        user.setId(9);
       user.setPhone("15770549440");
       user.setName("leopard");
        try {
            int temp=session.Delete(user);
            System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void InsertSqlTest(){
        User user=new User();
     //   user.setId(9);
        user.setPhone("15770549440");
        user.setName("leopard");
      //  user.setAddress("China");
       // System.out.println("Sql value："+FieldUtil.getAllColumnName_Value(user));
        Sql insert=new CreateTableSql(User.class);

        System.out.println("Sql 语句："+insert.getSql());
        System.out.println("Sql value："+insert.getValues());
       *//* for(int i=0;i<insert.getValues().size();++i){

            System.out.print(insert.getValues().get(i)+" ");
        }*//*


    }

    @Test
    public void tableUTest(){

        System.out.println(TableUtil.getAllTableName("com.leopardframework").size());
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        System.out.println(xmlpath);
    }

    @Test
    public void newTest(){
        SqlSession session=SessionFactory.openSession();
        User user=new User();
      //  user.setId(100);
        user.setPhone("15770549440");
       // user.setName("newleopard");
      //  user.setAddress("GXF");
        try {
            int temp=session.Save(user);
           *//* for (User u :temp){
                System.out.println(u.toString());
            }*//*
            System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void pageTest(){
        SqlSession session=SessionFactory.openSession();
        try {
            PageInfo temp=session.Get(User.class,3,8);
            List <User>users=temp.getList();
            for (User u :users){
                System.out.println(u.toString());
            }
            temp.description();
           //  System.out.println(" 结果："+temp);
            session.closeSession();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
   /* @Test
    public void createTableTest(){

        try {
            SqlSession session=SessionFactory.openSession("classpath:config.xml");
            User u=new User();
            u.setId(10086);
            System.out.println("第二次"+session.Get(u));
            session.Stop();
            System.out.println(session.Get(User.class));
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void TableTest(){
       Set set=ClassUtil.getClassSetByPackagename(SessionFactory.Config.getEntityPackage());
       System.out.println(set);
       System.out.println(new Date(12));
    }*/
    @Test
    public void xmlTest(){
        XmlFactoryBuilder builder=new XmlFactoryBuilder(ClassLoader.getSystemResource("config.xml").getPath());
        XmlFactoryBuilder.XmlFactory factory=builder.getFactory();
        DBPlugin db=(DBPlugin) factory.getBean("dataSource");
        Connection conn=db.getConn();
        String sql="select k.column_name FROM information_schema.table_constraints t\n" +
                "JOIN information_schema.key_column_usage k\n" +
                "USING (constraint_name,table_name)\n" +
                "WHERE t.constraint_type='PRIMARY KEY'\n" +
                "  AND t.table_name='student'";
        try {
            Statement stm=conn.createStatement();
            ResultSet res=stm.executeQuery(sql);
            while (res.next()){
                System.out.println(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   /* @Test
    public void Test(){
        SqlSession session=SessionFactory.openSession("classpath:config.xml");  //获取session  传入我们的配置文件
            User user=new User();
            user.setId(100868);
            user.setName("Leopard");
            user.setPhone("10010");
            user.setAddress("China");
            List list=new ArrayList();
            list.add(user);
        try {
            session.Save(user);  //传一个具体的对象
            session.SaveMore(list);  //多个对象放入list 好比批量操作，实际上并没有用到批量
            session.Delete(user); //删除条件即为对象的数据
            session.Delete(User.class, 10086, 10010, 10000); //根据唯一主键删除数据 ,传一个或多个主键值
            session.Update(user,10086);//根据主键修改数据  目标数据是该对象里的数据
            session.Get(User.class); // 查询所有数据
            session.Get(user);   //查询单条数据 查询条件即为对象的数据  如果匹配到多条数据，则只返回第一条
            session.Get(User.class,10000,10086);// 一样按主键查找
            session.Get(User.class,"where id=? order by id desc",10086);  //自定义条件查询 动态sql
            session.Get(User.class,1,5);  //分页查询  查询第一页数据  每页显示5 条数据 PageInfo来接收（下次再介绍）
            session.Get("","");  //自定义动态sql 返回的是结果集
            session.Stop();  //每执行完一次都要将其暂停
            session.Close();  // 关闭此次Session 下次要用时要重新获取

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

   /* @Test
    public void GenTest(){
        Factory factory=new SessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        try {
            System.out.println(session.Get(User.class).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
       *//* Jqubian jj=new Jqubian();
        jj.setGxfId(111);
        jj.setGxfLong(5454);
        jj.setGxfName("GGG");
        jj.setGxfPrice(2.5);
        jj.setGxfPrice2(2.5f);
        jj.getGxfStatus(TRUE.equals(true));
        jj.setGxfCreate(new Date(2018,02,05));
        System.out.println(jj);*//*
    }*/

    @Test
    public void GeneratorTest(){
        GeneratorFactory factory=Factory.getGeneratorFactory("classpath:config.xml");
        try {
           factory.openGenerator();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Add(int a,int b) throws SqlSessionException {
        if(a<b){
            throw new SqlSessionException("a<b");
        }else if(a<0){
            throw new SqlSessionException("a<0");
        }
        System.out.println(a-b);
    }

    @Test
    public void AddTest(){
        try {
            Add(3,2);
        } catch (SqlSessionException e) {e.printStackTrace();

        }
    }

    @Test
    public void JdbcTest(){
        XmlFactoryBuilder builder=new XmlFactoryBuilder(ClassLoader.getSystemResource("config.xml").getPath());
        XmlFactoryBuilder.XmlFactory factory=builder.getFactory();
        DBPlugin db=(DBPlugin) factory.getBean("dataSource");
        Connection conn;
        conn=db.getConn();
        System.out.println(db.getConn());
        String sql="insert into user (id,name) values (100088,'GGGGGGG')";
        try {
            conn.setAutoCommit(false);
            Statement stm=conn.createStatement();
            int temp=stm.executeUpdate(sql);
                System.out.println("结果:" +temp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/** 事物 success */
    @Test
    public void TranTest(){
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        User user=new User();
        user.setId(199997);
        user.setName("JJJ");
        user.setAddress("江西");
        Student su=new Student();
        su.setName("GXFJJJ");
        try {
            session.Save(su);
            int temp=session.Save(user);
            System.out.println("结果："+temp);
            session.Commit();
            session.Stop();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void PageTest(){
       /* 目前仅封装了我们开发中常用的一些数据信息。
        获取分页信息 ：getPage();  // 获取当前查询的页数
        getTotalPages(); //获取总页数
        getPageSize();   //获取每页显示的数据数量
        getTotalRows();  //获取总记录数
        getList();       //获取目标数据，也就是我们要查询的数据*/
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        try {
            PageInfo temp=session.Get(User.class,3,10);
            session.Stop();
            session.Close();
            List<User> users=temp.getList();
            for (User u :users){
                System.out.println(u.toString());
            }
            temp.description();
            //  System.out.println(" 结果："+temp);
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void SqlTest(){
        User user=new User();
        user.setId(10);
        user.setPhone("15770549440");
        user.setName("leopard");
          user.setAddress("China002");
        // System.out.println("Sql value："+FieldUtil.getAllColumnName_Value(user));

        /*Sql insert=new CreateTableSql(Article.class);

        System.out.println("Sql 语句："+insert.getSql());
        System.out.println("Sql value："+insert.getValues());*/
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        Article a=new Article();
        a.setId(1003);
        a.setName("GGG");
        a.setUser(user);
        try {
            System.out.println(session.Get(a));
            session.Commit();
            session.Stop();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void SQLTTt(){
        User user=new User();
        user.setId(8);
        user.setPhone("15770549440");
        user.setName("leopard");
        user.setAddress("China");
        Sql selectsql=new SelectSql(user);
        System.out.println("Sql 语句："+selectsql.getSql());
        System.out.println("Sql value："+selectsql.getValues());
    }

    @Test
    public void JionSqlTt(){

        /*Sql jionsql=new JoinSql(Article.class,User.class);
        System.out.println("Sql 语句："+jionsql.getSql());
        System.out.println("Sql value："+jionsql.getValues());*/
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        try {
            PageInfo pg=session.Get(Article.class,User.class,1,9);
            List<Article> list=pg.getList();
            for (Object a:list){
                System.out.println("结果："+a);
            }
            System.out.println();
            pg.description();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }

    public void Tt(){

        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        try {
            PageInfo pg=session.Get(Article.class,User.class,1,9);
            List<Article> list=pg.getList();
            for (Object a:list){
                System.out.println("结果："+a);
            }
            pg.description();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }
    @Test
    public void JionTt(){
        this.Tt();
        SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
        SqlSession session=factory.openSession();
        try {
            PageInfo pg=session.Get(Article.class,1,9);
            List<Article> list=pg.getList();
            for (Article a:list){
                System.out.println("结果："+a);
            }
            System.out.println();
            pg.description();
        } catch (SqlSessionException e) {
            e.printStackTrace();
        }

    }
}
