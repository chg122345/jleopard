package org.jleopard.test;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.Table;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Sep 3, 2018 4:48:16 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
@Table
public class Reply {
	
	@Column(isPrimary = EnumPrimary.YES)
	private Long id;
	@Column
	private String content;
	@Column(relation = User.class)
	private User user_id;
	private Article article_id;
	
	public Reply() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public User getUser_id() {
		return user_id;
	}

	public void setUser_id(User user_id) {
		this.user_id = user_id;
	}

	public Article getArticle_id() {
		return article_id;
	}

	public void setArticle_id(Article article_id) {
		this.article_id = article_id;
	}
	
	

}
