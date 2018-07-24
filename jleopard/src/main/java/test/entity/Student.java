package test.entity ;

import java.sql.Timestamp;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.Table;




/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  Leopard Generator
 * @DateTime  2018-04-18 08:50:20
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