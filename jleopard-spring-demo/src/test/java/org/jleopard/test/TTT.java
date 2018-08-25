package org.jleopard.test;
import java.util.ArrayList;
import java.util.List;

import org.jleopard.core.Factory;
import org.jleopard.exception.SqlSessionException;
import org.jleopard.model.User;
import org.jleopard.session.SqlSession;
import org.jleopard.session.sessionFactory.SessionFactory;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Aug 25, 2018 3:05:18 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public class TTT {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {
		
		SessionFactory factory=Factory.getSessionFactory("classpath:config.xml"); //获取sessionFactory  传入我们的配置文件路径
		SqlSession session=factory.openSession();
		     User user=new User();
	         user.setId(10086);
	         user.setName("JLeopard");
	         user.setPhone("10010");
	         user.setAddress("China");
	         List list=new ArrayList();
	         list.add(user);
	         
	        try {  //所有操作均有SqlSessionException异常
	        	
	            session.Save(user);  //传一个具体的实例对象
	            
	            session.SaveMore(list);  //多个实例对象放入list 批量操作存入数据库
	            
	            session.Delete(user); //删除条件即为实例对象的数据 如 ：上述user对象 删除的条件即为: delete from user表 where id = 10086 and name= 'jLeopard' and phone = ‘10010’ and address = 'China'
	            
	            session.Delete(User.class, 10086, 10010, 10000); //根据唯一主键删除数据 ,传一个或多个主键值 如：delete from user表 where id in (10086,10010,10000)
	            
	            session.Update(user,10086);//根据主键修改数据  目标数据是该对象里的数据 如： update user表 set name = xx（user对象的数据，该字段为空则跳过，不修改数据）where id = 10086
	            
	            session.Get(User.class); // 查询所有user表中的数据 return List<T>
	            
	            session.Get(user);   //查询单条数据 查询条件即为对象的数据  如果匹配到多条数据，则只返回第一条.。上述user对象 查询的条件即为: select 所有字段名  from user表 where id = 10086 and name= 'jLeopard' and phone = ‘10010’ and address = 'China'
	   
	            session.Get(User.class,10000,10086);// 按主键查找 select xx from user表 where id in ( 10000,10086)
	            
	            session.Get(User.class,"where id=? order by id desc",10086);  //自定义条件查询 动态参数
	            
	            session.Get(User.class,1,5);  //分页查询  查询第一页数据  每页显示5 条数据 PageInfo来接收（下问文详细介绍）
	            
	            session.Get("select * from user where id = ? and name =?",10010,"jLeopard");  //自定义动态sql 返回的是ResultSet结果集。 


	            session.Commit();  //每一次对更新数据库操作都要提交事物  不然数据不会写入数据库
	            
	            session.Stop();  //每执行完一次都要将其暂停
	            
	            session.Close();  //关闭此次sqlSession 下次要用时要重新获取
	            
	        } catch (SqlSessionException e) {
	            e.printStackTrace();
	        }

	}

}
