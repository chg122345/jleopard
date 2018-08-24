package org.jleopard.model ;

import java.sql.Timestamp;
import org.jleopard.core.annotation.*;
import org.jleopard.core.EnumPrimary;


/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  JLeopard Generator
 * @DateTime  2018-07-24 14:04:09
 */
@Table("student")
public class Student {

	@Column(value="ID",isPrimary = EnumPrimary.YES)
	private Integer id;
	@Column("NAME")
	private String name;
	@Column("CREATED")
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