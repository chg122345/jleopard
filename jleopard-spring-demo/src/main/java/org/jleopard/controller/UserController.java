package org.jleopard.controller;

import java.util.List;

import org.jleopard.dao.UserDao;
import org.jleopard.exception.SqlSessionException;
import org.jleopard.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 31, 2018 12:29:30 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
@RestController
public class UserController {

	@Autowired
	UserDao userDao;
	
	@GetMapping("/user")
	public User save() throws SqlSessionException {
		User user = new User();
		user.setName("测试spring");
		user.setId(9445);
		user.setAddress("江西南昌");
		user.setPhone("10086");
		System.out.println(userDao.save(user));
		return user;
	}
	
	@GetMapping("/user/list")
	public List<User> list() throws SqlSessionException {
		return userDao.list();
	}
}
