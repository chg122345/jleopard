package org.jleopard.spring;

import javax.sql.DataSource;

import org.jleopard.session.Configuration;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 23, 2018 3:13:24 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public class SqlSessionFactoryBean {

	private DataSource dataSource;
	
	private Configuration configuration;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	
}
