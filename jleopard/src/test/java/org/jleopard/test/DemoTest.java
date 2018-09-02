package org.jleopard.test;

import org.jleopard.core.sql.CreateTableSql;
import org.jleopard.core.sql.JoinSql;

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
	        user.setId("11111");
	        user.setName("leopard");
	        user.setPassword("123456");
	        CreateTableSql insert = new CreateTableSql(User.class);
	        System.out.println("Sql 语句："+insert.getSql());
	        JoinSql jsql = new JoinSql(Article.class, User.class);
	        System.out.println(jsql.getSql());
	       // System.out.println("Sql value："+insert.getValues());
		
	//	System.out.println((User.class == Object.class));
	}
}
