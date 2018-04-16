package com.leopardframework.test.entity ;

import com.leopardframework.core.annotation.*;
import com.leopardframework.core.enums.Primary;


/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  Leopard Generator
 * @DateTime  2018-04-16 09:35:52
 */
@Table
public class User {

	@Column(isPrimary = Primary.YSE)
	private Integer id;
	@Column
	private String name;
	@Column
	private String phone;
	@Column
	private String address;

	public User() {
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
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}