package org.jleopard.jdbc;
/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 24, 2018 10:17:19 AM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

import javax.sql.DataSource;


public class BaseDataSource implements DataSource{
	
	private Connection conn;
	
	private String url;
	
	private String username;
	
	private String password;
	
	private String driver = "com.mysql.jdbc.Driver";
	
	public BaseDataSource() {
		super();
	}

	public BaseDataSource(String url, String user, String password, String driverClass) {
		super();
		this.url = url;
		this.username = user;
		this.password = password;
		this.driver = driverClass;
	}

	public BaseDataSource(String url, String user, String password) {
		super();
		this.url = url;
		this.username = user;
		this.password = password;
	}
	
	public Connection getConnection() {
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

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getLoginTimeout() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Connection getConnection(String username, String password) throws SQLException {
		try {
			Class.forName(driver);
			conn=DriverManager.getConnection(url,username,password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
