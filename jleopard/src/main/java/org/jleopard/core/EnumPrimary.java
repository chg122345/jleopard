package org.jleopard.core;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 23, 2018 4:07:13 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public enum EnumPrimary {
	
	 NO(0,"普通主键"),
	 YES(1,"普通主键"),
	 AUTOINCREMENT(2,"普通主键");
	
	 
	private int code;
	private String id;
	
	
	private EnumPrimary() {
	}
	private EnumPrimary(int code, String id) {
		this.code = code;
		this.id = id;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

}
