package test.entity ;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.Table;



/**
 *
 * @Copyright  (c) by Chen_9g (80588183@qq.com).
 * @Author  Leopard Generator
 * @DateTime  2018-04-18 08:50:20
 */
@Table("user")
public class User {

	@Column(value="ID",isPrimary = EnumPrimary.YES)
	private Integer id;
	@Column("NAME")
	private String name;
	@Column("PHONE")
	private String phone;
	@Column(value = "ADDRESS",allowNull = true)
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

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", phone='" + phone + '\'' +
				", address='" + address + '\'' +
				'}';
	}
}