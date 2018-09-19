/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-09-19  上午11:41
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.core;

import java.util.Collection;

/**
 * 数据表对象
 */
public class TableProperties {

    private String tableName;

    private String pkName;

    private Collection<String> fkNames;

    private Collection<String>  columns;

    public TableProperties() {
    }

    public TableProperties(String tableName, String pkName, Collection<String> fkNames, Collection<String> columns) {
        this.tableName = tableName;
        this.pkName = pkName;
        this.fkNames = fkNames;
        this.columns = columns;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPkName() {
        return pkName;
    }

    public void setPkName(String pkName) {
        this.pkName = pkName;
    }

    public Collection<String> getFkNames() {
        return fkNames;
    }

    public void setFkNames(Collection<String> fkNames) {
        this.fkNames = fkNames;
    }

    public Collection<String> getColumns() {
        return columns;
    }

    public void setColumns(Collection<String> columns) {
        this.columns = columns;
    }
}
