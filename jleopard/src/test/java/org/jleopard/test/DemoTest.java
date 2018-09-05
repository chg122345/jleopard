package org.jleopard.test;

import java.util.ArrayList;
import java.util.List;

import org.jleopard.core.Factory;
import org.jleopard.core.sql.CreateTableSql;
import org.jleopard.core.sql.DeleteSql;
import org.jleopard.core.sql.DeleteSqlMore;
import org.jleopard.core.sql.InsertSql;
import org.jleopard.core.sql.JoinSql;
import org.jleopard.core.sql.SelectSql;
import org.jleopard.core.sql.SelectSqlMore;
import org.jleopard.core.sql.UpdateSql;
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
	        user.setName("jleopardDEMO");
	        user.setPassword("123456789");
	        Reply r = new Reply();
	        r.setId(3L);
	        r.setContent("content ..");
	        r.setUser_id(user);
	        List<Reply> list = new ArrayList<>();
	        list.add(r);
	        Article a = new Article();
	        a.setId(Long.valueOf(101));
	        a.setTitle("这是标题");
	        a.setStatus(Byte.valueOf("1"));
	        a.setUser_id(user);
	        a.setReplys(list);
			r.setArticle_id(a);
	        JoinSql insert = new JoinSql(Article.class, new Class<?>[]{User.class,Reply.class});
	        System.out.println("Sql 语句："+insert.getSql());
//	        JoinSql jsql = new JoinSql(Article.class, Reply.class,User.class,Post.class);
//	        System.out.println(jsql.getSql());
//	        System.out.println("Sql value："+insert.getPkValue());
		
	//	System.out.println((User.class == Object.class));
	        SessionFactory factory= Factory.getSessionFactory("classpath:config.xml");
	        SqlSession session=factory.openSession();
	        try {
	        //	System.out.println(session.getToPage(User.class,1,2,"where name = ?","leopard").getList());
	        	System.out.println(session.getByJoin(Article.class, new Class<?>[]{User.class},"article.id=?",101));
	        	session.commit();
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
