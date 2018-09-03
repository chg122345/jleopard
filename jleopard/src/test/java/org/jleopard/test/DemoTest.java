package org.jleopard.test;

import org.jleopard.core.Factory;
import org.jleopard.core.sql.CreateTableSql;
import org.jleopard.exception.SqlSessionException;
import org.jleopard.session.SqlSession;
import org.jleopard.session.sessionFactory.SessionFactory;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Sep 2, 2018 5:44:08 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public class DemoTest {

	public static void main(String[] args) {
		 User user=new User();
	        user.setId("1111123");
	        user.setName("leopard");
	        user.setPassword("123456");
	       CreateTableSql insert = new CreateTableSql(Article.class);
	        System.out.println("Sql 语句："+insert.getSql());
//	        JoinSql jsql = new JoinSql(Article.class, Reply.class,User.class,Post.class);
//	        System.out.println(jsql.getSql());
	       // System.out.println("Sql value："+insert.getValues());
		
	//	System.out.println((User.class == Object.class));
	        SessionFactory factory= Factory.getSessionFactory("classpath:config.xml");
	        SqlSession session=factory.openSession();
	        try {
	        	session.Save(user);
	        	session.Commit();
	           /* PageInfo pg=session.Get(Article.class,User.class,1,9);
	            List<Article> list=(List<Article>) pg.getList();
	            for (Object a:list){
	                System.out.println("结果："+a);
	            }
	            System.out.println();*/
	        } catch (SqlSessionException e) {
	            e.printStackTrace();
	        }
	}
}
