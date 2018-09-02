package org.jleopard.test;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.Table;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Sep 2, 2018 5:36:26 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
@Table
public class User {
	
	@Column(isPrimary=EnumPrimary.YES)
	private String id;
	@Column
	private String name;
	@Column
	private String password;
	
	public User() {
		// TODO Auto-generated constructor stub
	}

	
	public User(String id, String name, String password) {
		super();
		this.id = id;
		this.name = name;
		this.password = password;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

}
