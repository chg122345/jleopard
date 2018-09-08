package org.jleopard.spring;

import javax.sql.DataSource;

import org.jleopard.generator.GeneratorFactory;
import org.jleopard.session.Configuration;
import org.jleopard.session.sessionFactory.SessionFactory;

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
	
	private String entityPackage;
	private String generatorPackage;
	private String generatorProject;
	private boolean useGeneratedKeys = false;
	private boolean useColumnLabel = false;
	private boolean autoCommit = true;
	private boolean dev = true;
	
	
	public SqlSessionFactoryBean() {
		
	}
	
	public SessionFactory getSessionFactory() {
		return SessionFactory.Builder.build(this.getConfiguration());
	}

	public GeneratorFactory getGeneratorFactory() {
		return GeneratorFactory.Builder.build(this.getConfiguration());
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Configuration getConfiguration() {
		return configuration == null ? new Configuration(this.getEntityPackage(), this.getGeneratorPackage(), this.getGeneratorProject(),
				this.getDataSource(), this.isUseGeneratedKeys(), this.isUseColumnLabel(), this.isAutoCommit(), this.isDev()) : configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public String getEntityPackage() {
		return entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public String getGeneratorPackage() {
		return generatorPackage;
	}

	public void setGeneratorPackage(String generatorPackage) {
		this.generatorPackage = generatorPackage;
	}

	public String getGeneratorProject() {
		return generatorProject;
	}

	public void setGeneratorProject(String generatorProject) {
		this.generatorProject = generatorProject;
	}

	public boolean isUseGeneratedKeys() {
		return useGeneratedKeys;
	}

	public void setUseGeneratedKeys(boolean useGeneratedKeys) {
		this.useGeneratedKeys = useGeneratedKeys;
	}

	public boolean isUseColumnLabel() {
		return useColumnLabel;
	}

	public void setUseColumnLabel(boolean useColumnLabel) {
		this.useColumnLabel = useColumnLabel;
	}

	public boolean isAutoCommit() {
		return autoCommit;
	}

	public void setAutoCommit(boolean autoCommit) {
		this.autoCommit = autoCommit;
	}

	public boolean isDev() {
		return dev;
	}

	public void setDev(boolean dev) {
		this.dev = dev;
	}
	
	
}
