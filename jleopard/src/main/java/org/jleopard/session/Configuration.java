package org.jleopard.session;

import javax.sql.DataSource;

import org.jleopard.util.ClassUtil;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 23, 2018 3:24:16 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public class Configuration {
	
	    protected String entityPackage;
	    protected String generatorPackage;
	    protected String generatorProject;
	    protected DataSource dataSource;
	    protected boolean useGeneratedKeys = false;
	    protected boolean useColumnLabel = false;
	    protected boolean autoCommit = true;
	    protected boolean dev = true;
	 
	    
	    public Configuration() {
	    }


	    
		public Configuration(String entityPackage, String generatorPackage, String generatorProject,
				DataSource dataSource, boolean useGeneratedKeys, boolean useColumnLabel, boolean autoCommit,
				boolean dev) {
			super();
			this.entityPackage = entityPackage;
			this.generatorPackage = generatorPackage;
			this.generatorProject = generatorProject;
			this.dataSource = dataSource;
			this.useGeneratedKeys = useGeneratedKeys;
			this.useColumnLabel = useColumnLabel;
			this.autoCommit = autoCommit;
			this.dev = dev;
		}

		public Configuration(String entityPackage, DataSource dataSource, boolean useGeneratedKeys,
				boolean useColumnLabel, boolean autoCommit, boolean dev) {
			super();
			this.entityPackage = entityPackage;
			this.dataSource = dataSource;
			this.useGeneratedKeys = useGeneratedKeys;
			this.useColumnLabel = useColumnLabel;
			this.autoCommit = autoCommit;
			this.dev = dev;
		}
		
		
		public Configuration(String generatorPackage, String generatorProject, DataSource dataSource) {
			super();
			this.generatorPackage = generatorPackage;
			this.generatorProject = generatorProject;
			this.dataSource = dataSource;
		}



		public Configuration(String entityPackage, DataSource dataSource) {
			super();
			this.entityPackage = entityPackage;
			this.dataSource = dataSource;
			this.useGeneratedKeys = true;
			this.useColumnLabel = true;
			this.autoCommit = true;
			this.dev = true;
		}
		
		public Configuration( DataSource dataSource) {
			super();
			this.entityPackage = ClassUtil.getClassPath();
			this.dataSource = dataSource;
			this.useGeneratedKeys = true;
			this.useColumnLabel = true;
			this.autoCommit = true;
			this.dev = true;
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



		public DataSource getDataSource() {
			return dataSource;
		}



		public void setDataSource(DataSource dataSource) {
			this.dataSource = dataSource;
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
