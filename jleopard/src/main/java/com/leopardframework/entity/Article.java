package com.leopardframework.entity ;

import org.jleopard.core.annotation.*;
import org.jleopard.core.EnumPrimary;


/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  JLeopard Generator
 * @DateTime  2018-07-24 13:50:47
 */
@Table("article")
public class Article {

	@Column(value="id",isPrimary = EnumPrimary.YES)
	private Integer id;
	@Column("name")
	private String name;
	@Column("user_id")
	private Integer userId;

	public Article() {
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
	
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}