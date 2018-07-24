package org.jleopard.jdbc;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * C3P0连接池配置
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class C3p0DataSource{
	
	private String url;
	private String username;
	private String password;
	private String driver = "com.mysql.jdbc.Driver";
	private int maxPoolSize = 100;
	private int minPoolSize = 10;
	private int initialPoolSize = 10;
	private int maxIdleTime = 20;
	private int acquireIncrement = 2;
	
	private ComboPooledDataSource dataSource;
	private volatile boolean isStarted = false;

	public C3p0DataSource() {
	}

	public C3p0DataSource setdriver(String driver) {
		if (driver==null||driver.equals(""))
			throw new IllegalArgumentException("driver can not be blank.");
		this.driver = driver;
		return this;
	}
	
	public C3p0DataSource setMaxPoolSize(int maxPoolSize) {
		if (maxPoolSize < 1)
			throw new IllegalArgumentException("maxPoolSize must more than 0.");
		this.maxPoolSize = maxPoolSize;
		return this;
	}
	
	public C3p0DataSource setMinPoolSize(int minPoolSize) {
		if (minPoolSize < 1)
			throw new IllegalArgumentException("minPoolSize must more than 0.");
		this.minPoolSize = minPoolSize;
		return this;
	}
	
	public C3p0DataSource setInitialPoolSize(int initialPoolSize) {
		if (initialPoolSize < 1)
			throw new IllegalArgumentException("initialPoolSize must more than 0.");
		this.initialPoolSize = initialPoolSize;
		return this;
	}
	
	public C3p0DataSource setMaxIdleTime(int maxIdleTime) {
		if (maxIdleTime < 1)
			throw new IllegalArgumentException("maxIdleTime must more than 0.");
		this.maxIdleTime = maxIdleTime;
		return this;
	}
	
	public C3p0DataSource setAcquireIncrement(int acquireIncrement) {
		if (acquireIncrement < 1)
			throw new IllegalArgumentException("acquireIncrement must more than 0.");
		this.acquireIncrement = acquireIncrement;
		return this;
	}
	
	public C3p0DataSource(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public C3p0DataSource(String url, String username, String password, String driver) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver != null ? driver : this.driver;
	}
	
	public C3p0DataSource(String url, String username, String password, String driver, Integer maxPoolSize, Integer minPoolSize, Integer initialPoolSize, Integer maxIdleTime, Integer acquireIncrement) {
		initC3p0Properties(url, username, password, driver, maxPoolSize, minPoolSize, initialPoolSize, maxIdleTime, acquireIncrement);
	}
	
	private void initC3p0Properties(String url, String username, String password, String driver, Integer maxPoolSize, Integer minPoolSize, Integer initialPoolSize, Integer maxIdleTime, Integer acquireIncrement) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.driver = driver != null ? driver : this.driver;
		this.maxPoolSize = maxPoolSize != null ? maxPoolSize : this.maxPoolSize;
		this.minPoolSize = minPoolSize != null ? minPoolSize : this.minPoolSize;
		this.initialPoolSize = initialPoolSize != null ? initialPoolSize : this.initialPoolSize;
		this.maxIdleTime = maxIdleTime != null ? maxIdleTime : this.maxIdleTime;
		this.acquireIncrement = acquireIncrement != null ? acquireIncrement : this.acquireIncrement;
	}
	
	public C3p0DataSource(File propertyfile) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(propertyfile);
			Properties ps = new Properties();
			ps.load(fis);
			
			initC3p0Properties(ps.getProperty("jdbc.Url"), ps.getProperty("jdbc.username"), ps.getProperty("jdbc.Password"), ps.getProperty("jdbc.driver"),
					toInt(ps.getProperty("maxPoolSize")), toInt(ps.getProperty("minPoolSize")), toInt(ps.getProperty("initialPoolSize")),
					toInt(ps.getProperty("maxIdleTime")),toInt(ps.getProperty("acquireIncrement")));
		} catch (Exception e) {
			throw e instanceof RuntimeException ? (RuntimeException)e : new RuntimeException(e);
		}
		finally {
			if (fis != null)
				try {fis.close();} catch (IOException e) {e.getMessage();}
		}
	}
	
	public C3p0DataSource(Properties properties) {
		Properties ps = properties;
		initC3p0Properties(ps.getProperty("jdbc.Url"), ps.getProperty("jdbc.username"), ps.getProperty("jdbc.Password"), ps.getProperty("jdbc.driver"),
				toInt(ps.getProperty("maxPoolSize")), toInt(ps.getProperty("minPoolSize")), toInt(ps.getProperty("initialPoolSize")),
				toInt(ps.getProperty("maxIdleTime")),toInt(ps.getProperty("acquireIncrement")));
	}
	
	public boolean start() {
		if (isStarted)
			return true;
		
		dataSource = new ComboPooledDataSource();
		dataSource.setJdbcUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		try {dataSource.setDriverClass(driver);}
		catch (PropertyVetoException e) {dataSource = null; System.err.println("C3p0DataSource start error"); throw new RuntimeException(e);} 
		dataSource.setMaxPoolSize(maxPoolSize);
		dataSource.setMinPoolSize(minPoolSize);
		dataSource.setInitialPoolSize(initialPoolSize);
		dataSource.setMaxIdleTime(maxIdleTime);
		dataSource.setAcquireIncrement(acquireIncrement);
		
		isStarted = true;
		return true;
	}
	
	private Integer toInt(String str) {
		return Integer.parseInt(str);
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	public Connection getConn() {
		try {
			this.start();
			return this.getDataSource().getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ComboPooledDataSource getComboPooledDataSource() {
		return dataSource;
	}
	
	public boolean stop() {
		if (dataSource != null)
			dataSource.close();
		
		dataSource = null;
		isStarted = false;
		return true;
	}
}

