package com.leopardframework.test.entity ;

import java.sql.Timestamp;
import com.leopardframework.core.annotation.*;
import com.leopardframework.core.enums.Primary;


/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  Leopard Generator
 * @DateTime  2018-04-16 09:35:51
 */
@Table
public class Student {

	@Column(isPrimary = Primary.YSE)
	private Integer id;
	@Column
	private String name;
	@Column
	private Timestamp created;

	public Student() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}
}