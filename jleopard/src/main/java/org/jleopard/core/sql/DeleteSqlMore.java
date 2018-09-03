package org.jleopard.core.sql;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.PathUtils;


/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class DeleteSqlMore implements Sql {

    private static final Log log=LogFactory.getLog(DeleteSqlMore.class);

    private String tableName;

    private Object primaryKeyName;

    public DeleteSqlMore(Class<?> cls){
        this.tableName =TableUtil.getTableName(cls);
        this.primaryKeyName=FieldUtil.getPrimaryKeys(cls).get(0);
    }
    /**
     *
     * @return
     *   DELETE FROM USER
     *     WHERE ID
     */
    @Override
    public String getSql() {
        StringBuilder SQL =new StringBuilder();
        SQL.append("delete from ").append(tableName).append(PathUtils.LINE).append(" ").append("where ").append(primaryKeyName);
        log.info(" 生成的sql语句: "+SQL.toString());
        return SQL.toString();
    }
}
