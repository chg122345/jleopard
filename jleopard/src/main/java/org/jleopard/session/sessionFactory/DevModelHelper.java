package org.jleopard.session.sessionFactory;

import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.ArrayUtil;
import org.jleopard.util.CollectionUtil;

import java.util.List;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 24, 2018 12:33:06 PM
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public final class DevModelHelper {

    private static final Log log = LogFactory.getLog(SessionDirectImpl.class);

    protected static void outParameter(boolean DevModel, String sql, @SuppressWarnings("rawtypes") List values) {
        if (CollectionUtil.isNotEmpty(values)) {
            outParameter(DevModel, sql, values.toArray());
        }else {
            outParameter(DevModel,sql,"");
        }

    }

    protected static void outParameter(boolean DevModel, String sql, Object[] values) {

        if (DevModel) {
            if (ArrayUtil.isNotEmpty(values)) {
                String args = "";
                for (Object value : values) {
                    args += value + " ";
                }
                log.info(String.format("当前执行的sql语句: \n \t %s \n Paramters: %s \n", sql, args));
            }else {
                outParameter(DevModel,sql,"");
            }
        }
    }

    protected static void outParameter(boolean DevModel, String sql, Object primaryKey) {
        if (DevModel) {
            if (primaryKey == null || "".equals(primaryKey)) {
                log.info(String.format("当前执行的sql语句: \n \t %s", sql));
            } else {
                log.info(String.format("当前执行的sql语句: \n \t %s \n Paramters: %s ", sql, primaryKey));
            }
        }
    }

}
