package com.leopardframework.entity ;

import org.jleopard.core.annotation.*;
import org.jleopard.core.EnumPrimary;


/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  JLeopard Generator
 * @DateTime  2018-07-24 13:50:47
 */
@Table("user")
public class User {

	@Column(value="ID",isPrimary = EnumPrimary.YES)
	private Integer id;
	@Column("NAME")
	private String name;
	@Column("PHONE")
	private String phone;
	@Column("ADDRESS")
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