package org.jleopard.test;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.Table;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Sep 2, 2018 5:39:20 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
@Table
public class Article {
	
	@Column(isPrimary=EnumPrimary.AUTOINCREMENT)
	private Long id;
	
	@Column
	private String title;
	
	@Column
	private Byte status;
	
	@Column(relation=User.class)
	private User user_id;

	public Article() {
		// TODO Auto-generated constructor stub
	}

	public Article(Long id, String title, Byte status, User user_id) {
		super();
		this.id = id;
		this.title = title;
		this.status = status;
		this.user_id = user_id;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public User getUser_id() {
		return user_id;
	}

	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}

	@Override
	public String toString() {
		return "Article [id=" + id + ", title=" + title + ", status=" + status + ", user_id=" + user_id + "]";
	}
	
	
}
