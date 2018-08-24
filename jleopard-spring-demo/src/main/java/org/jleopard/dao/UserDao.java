package org.jleopard.dao;

import java.util.List;

import org.jleopard.exception.SqlSessionException;
import org.jleopard.model.User;
import org.jleopard.session.SqlSession;
import org.jleopard.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 24, 2018 1:57:10 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
@Component
public class UserDao {

	@Autowired
	private SqlSessionFactoryBean sessionFactoryBean;
	
	public int save(User user) throws SqlSessionException {
		SqlSession session =sessionFactoryBean.getSessionFactory().openSession();
		int temp = session.Save(user);
		session.Commit();
		session.Stop();
		return temp;
	}
	
	public List<User> list() throws SqlSessionException{
		SqlSession session =sessionFactoryBean.getSessionFactory().openSession();
		List<User> list = session.Get(User.class);
		session.Stop();
		return list;
	}
}

