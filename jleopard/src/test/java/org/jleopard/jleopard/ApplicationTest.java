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
import org.jleopard.generator.GeneratorFactory;
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
	            System.out.println(session.Get(user));
	            session.Commit();
	            session.Stop();
	        } catch (SqlSessionException e) {
	            e.printStackTrace();
	        }


	    }
	 
	 @Test
	    public void GeneratorTest(){
	        GeneratorFactory factory=Factory.getGeneratorFactory("classpath:config.xml");
	        try {
	           factory.openGenerator();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

}
