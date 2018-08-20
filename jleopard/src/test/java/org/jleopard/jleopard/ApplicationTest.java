package org.jleopard.jleopard;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jleopard.core.Factory;
import org.jleopard.core.sql.CreateTableSql;
import org.jleopard.core.sql.SelectSql;
import org.jleopard.core.sql.Sql;
import org.jleopard.exception.SqlSessionException;
import org.jleopard.jdbc.BaseDataSource;
import org.jleopard.session.SqlSession;
import org.jleopard.session.sessionFactory.SessionFactory;
import org.jleopard.xml.XmlFactoryBuilder;
import org.junit.Test;

import test.entity.Article;
import test.entity.User;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 24, 2018 11:03:11 AM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public class ApplicationTest {
	
	 @Test
	    public void xmlTest(){
	        XmlFactoryBuilder builder=new XmlFactoryBuilder(ClassLoader.getSystemResource("config.xml").getPath());
	        XmlFactoryBuilder.XmlFactory factory=builder.getFactory();
	        BaseDataSource db=(BaseDataSource) factory.getBean("dataSource");
	        Connection conn=db.getConnection();
	        String sql="select k.column_name FROM information_schema.table_constraints t\n" +
	                "JOIN information_schema.key_column_usage k\n" +
	                "USING (constraint_name,table_name)\n" +
	                "WHERE t.constraint_type='PRIMARY KEY'\n" +
	                "  AND t.table_name='user'";
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
	 
	 @Test
	    public void SQLTTt(){
	        User user=new User();
	        user.setId(8);
	        user.setPhone("15770549440");
	        user.setName("leopard");
	        user.setAddress("China");
	        SelectSql selectsql=new SelectSql(user);
	        System.out.println("Sql 语句："+selectsql.getSql());
	        System.out.println("Sql value："+selectsql.getValues());
	    }
	 
	 @Test
	    public void SqlTest(){
	        User user=new User();
	        user.setId(10);
	        user.setPhone("15770549440");
	        user.setName("leopard");
	          user.setAddress("China002");
	        // System.out.println("Sql value："+FieldUtil.getAllColumnName_Value(user));

	          Sql insert=new CreateTableSql(Article.class);

	        System.out.println("Sql 语句："+insert.getSql());
	       SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
	        SqlSession session=factory.openSession();
	        Article a=new Article();
	      //  a.setId(1003);
	        a.setName("GGG");
	        a.setUser(user);
	        try {
	            System.out.println(session.Get(Article.class,User.class,""));
	            session.Commit();
	            session.Stop();
	        } catch (SqlSessionException e) {
	            e.printStackTrace();
	        }


	    }
	 
	/* @Test
	    public void GeneratorTest(){
	        GeneratorFactory factory=Factory.getGeneratorFactory("classpath:config.xml");
	        try {
	           factory.openGenerator();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	 
	 #Jleopard框架是什么？
	  1. jleopard是一款完全orm框架，像我们熟悉的Hibernate一样，封装好了大量的sql，足以让我们解放双手，达到极速开发体验。
	  2. jleopard目前能实现基本的CRUD，包括多表外键链接，关联查询，分页查询，逆向工程。
	  3. 使用jleopard开发只需配置好数据源，实体类扫描包路径即可，其余全采用注解自动处理。

	[hr]
	  1. 从配置文件开始 
	[pre]
	<?xml version="1.0" encoding="UTF-8" ?>
	<!DOCTYPE jleopard-configuration  PUBLIC "-// jleopard.org//DTD Config 1.0//EN"
	        "http://www.jleopard.org/dtd/jleopard.dtd">
	        <jleopard-configuration>
	        <!-- 配置扫描实体类所在包 -->
	        <config>
	        <entityScan value="com.leopardframework.entity"></entityScan>
	        <dev value="true"></dev>
	        </config>
	        <!-- 逆向工程生成javabean的路径配置 -->
	        <generator>
	       <target package="com.leopardframework.entity" project="/src/"/>
	        </generator>
	        <!-- 数据原配置 id="dataSource" 不能更改 -->
	        <dataSource class="org.jleopard.jdbc.BaseDataSource" id="dataSource">
	         <property name="driver" value="com.mysql.jdbc.Driver"/>
	        <property name="url" value="jdbc:mysql://127.0.0.1:3306/jleopardDemo?characterEncoding=UTF-8"/>
	        <property name="username" value="root"/>
	        <property name="password" value="chg122345"/>
	        </dataSource>
	        </jleopard-configuration>
	[/pre]

	  2. 在实体类上标注注解
	   @Table("tablename") 注解在类上 value 值为数据库对应的表名
	   @Column(value="colunmName",isPrimary = EnumPrimary.YES , allowNull = false , relation= "")
	   value对应数据库的字段名，isPrimary是否为主键，有三种类型（不是主键，是主键，自增主键，allowNull是否允许为空，relation作为外键连接哪一个字段名
	[pre]
	@Table("user")
	public class User {
		@Column(value="ID",isPrimary = EnumPrimary.YES )
		private Integer id;
		@Column("NAME")
		private String name;
		@Column("PHONE")
		private String phone;
		@Column("ADDRESS")
		private String address;
	       //省略....
	}

	@Table("article")
	public class Article {

	    @Column(isPrimary = EnumPrimary.AUTOINCREMENT)
	    private int id;
	    @Column(allowNull = true)
	    private String name;
	    //把user_id作为外键连接user表的id
	    @Column(value = "user_id",relation = "user_id")
	    private User user;
	     //省略....
	[/pre]

	  3. 获取sqlSession操作数据库表
	   （1）写一个工具类获取sessionFactory
	[pre]
	public static SessionFactory getSessionFactory() {
	                  //传入配置文件路径
			  SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");
			  return factory;
		}
	[/pre]
	   （2）获取sqlSession进行操作
	[pre]
	public int save(User user) throws SqlSessionException {
			SqlSession session =Getsession.getSessionFactory().openSession();
			int temp = session.Save(user);
			session.Commit();
			session.Stop();
			return temp;
		}
	[/pre]
	[hr]
	  4. 所有方法操作详解
	[pre]
	@Test
	public void Test(){
	    SessionFactory factory=Factory.getSessionFactory("classpath:config.xml"); //获取session  传入我们的配置文件
	    SqlSession session=factory.openSession();
	        User user=new User();
	        user.setId(10086);
	        user.setName("Leopard");
	        user.setPhone("10010");
	        user.setAddress("China");
	        List list=new ArrayList();
	        list.add(user);
	    try {   //所有操作均有SqlSessionException异常
	        session.Save(user);  //传一个具体的对象
	        session.SaveMore(list);  //多个对象放入list 好比批量操作，实际上并没有用到批量
	        session.Delete(user); //删除条件即为对象的数据
	        session.Delete(User.class, 10086, 10010, 10000); //根据唯一主键删除数据 ,传一个或多个主键值
	        session.Update(user,10086);//根据主键修改数据  目标数据是该对象里的数据
	        session.Get(User.class); // 查询所有数据
	        session.Get(user);   //查询单条数据 查询条件即为对象的数据  如果匹配到多条数据，则只返回第一条
	        session.Get(User.class,10000,10086);// 一样按主键查找
	        session.Get(Article.class,User.class,"") //查询article和所属user 关联查询
	        session.Get(User.class,"where id=? order by id desc",10086);  //自定义条件查询 动态sql
	        session.Get(User.class,1,5);  //分页查询  查询第一页数据  每页显示5 条数据 PageInfo来接收（下问文详细介绍）
	        session.Get("","");  //自定义动态sql 返回的是结果集


	        session.Commit();  //每一次对更新数据库操作都要提交事物  不然数据不会写入数据库
	        session.Stop();  //每执行完一次都要将其暂停
	        session.Close();  // 关闭此次Session 下次要用时要重新获取
	       
	    } catch (SqlSessionException e) {
	        e.printStackTrace();
	    }

	}

	[/pre]
	分页详解
	[pre]
	@Test
	public void PageTest(){
	    目前仅封装了我们开发中常用的一些数据信息。
	    获取分页信息 ：getPage();  // 获取当前查询的页数
	    getTotalPages(); //获取总页数
	    getPageSize();   //获取每页显示的数据数量
	    getTotalRows();  //获取总记录数
	    getList();       //获取目标数据，也就是我们要查询的数据
	    SessionFactory factory=Factory.getSessionFactory("classpath:config.xml");  //获取session工厂
	    SqlSession session=factory.openSession();  //打开session连接 开始操作
	    try {
	        PageInfo temp=session.Get(User.class,3,10);  //分页查询开始 用封装好的pageInfo接收查询结果
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
	[/pre]*/

	/****************************************************/
	/*  github = github.com/chg122345/jleopard   */
	/*  QQ = 80588183                                         */
	/*  jleopard暂时还没有maven依赖                  */
	/*  github有发布的版本开发jar包                     */
	/***************************************************/
}
