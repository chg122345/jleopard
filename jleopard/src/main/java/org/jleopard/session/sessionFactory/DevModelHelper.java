package org.jleopard.session.sessionFactory;

import java.util.List;

import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 24, 2018 12:33:06 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public final class DevModelHelper {
	private static final Log LOG = LogFactory.getLog(SessionDirectImpl.class);
	
	protected static void outParameter(boolean DevModel,String sql,@SuppressWarnings("rawtypes") List values){
        if(DevModel){
        	String args = "";
            for (Object value:values){
                args += value + " ";
            }
            LOG.info("当前执行的sql语句: \n \t" + sql + "\n Paramters: " + args + "\n");
        }
    }
    protected static void outParameter(boolean DevModel,String sql,Object[] values){
        if(DevModel){
        	String args = "";
            for (Object value:values){
                args += value + " ";
            }
            LOG.info("当前执行的sql语句: \n \t" + sql + "\n Paramters: " + args + "\n");
        }    
    }

    protected static void outParameter(boolean DevModel, String sql, Object primaryKey) {
        if(DevModel){
        	if (primaryKey == null || "".equals(primaryKey)) {
        		LOG.info("当前执行的sql语句: \n \t" + sql + "\n");
        	}else {
        		LOG.info("当前执行的sql语句: \n \t" + sql + "\n Paramters: " + primaryKey + "\n");
        	}
        }
    }

}
