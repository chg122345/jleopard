package com.leopardframework.plugins;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 
 * 数据库连接信息配置
 *
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class DBPlugin {
	
	private String driver="com.mysql.jdbc.Driver";
	private String url;
	private String username;
	private String password;
	private Connection conn;

	public DBPlugin(String Driver,String Url,String Username,String Password) {
		this.driver = Driver != null ? Driver : this.driver;
		this.url=Url;
		this.username=Username;
		this.password=Password;
	}
	public void initDBPlugin(String Driver,String Url,String Username,String Password) {
		this.driver = Driver != null ? Driver : this.driver;
		this.url=Url;
		this.username=Username;
		this.password=Password;
	}
	public DBPlugin(Properties ps) {
		initDBPlugin(ps.getProperty("jdbc.DriverClass"),ps.getProperty("jdbc.Url"), ps.getProperty("jdbc.User"), ps.getProperty("jdbc.Password"));
	}
	public DBPlugin(String Url,String Username,String Password) {
		this.url=Url;
		this.username=Username;
		this.password=Password;
	}
	
	public Connection getConn() {
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,username,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	public boolean stop() {
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	  }
		return true;
	}
	
}
