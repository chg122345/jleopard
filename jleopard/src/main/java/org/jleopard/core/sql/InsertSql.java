package org.jleopard.core.sql;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jleopard.core.util.FieldFilterUtil;
import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.MapUtil;
import org.jleopard.util.PathUtils;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class InsertSql<T> implements Sql, CloumnNames, CloumnValue {

	private static final Log log = LogFactory.getLog(InsertSql.class);

	private String tableName; // 表名

	private List<String> columnNames; // 字段名

	private List<Object> values; // 字段对应的值

	/**
	 *
	 * @param tableName
	 * @param map
	 */
	public InsertSql(String tableName, Map<String, Object> map) {
		init(tableName, map);
	}

	/**
	 *
	 */
	public InsertSql(T entity) {
		Map<String, Object> map = FieldUtil.getColumnName_Value(entity);
		Map<String, Field> columnField = FieldUtil.getAllColumnName_Field(entity.getClass());
		if (MapUtil.isEmpty(map)) {
			log.error("取到的对象值为空...");
		}
		init(TableUtil.getTableName(entity), FieldFilterUtil.filterListFeild(columnField, map));
	}

	private void init(String tableName, Map<String, Object> map) {
		List<String> columns = new ArrayList<>();
		List<Object> vs = new ArrayList<>();
		this.tableName = tableName;
		map.forEach((c, v) -> {
			columns.add(c);
			vs.add(v);
		});
		this.columnNames = columns;
		this.values = vs;
	}

	/**
	 *
	 * @return 字段名
	 */
	@Override
	public List<String> getColumnNames() {
		return columnNames;
	}

	/**
	 * @return 字段名对应的 value
	 */
	@Override
	public List<Object> getValues() {
		return values;
	}

	/**
	 * 获取拼接好的sql
	 * 
	 * @return 例: INSERT INTO USER(PHONE,ID,NAME) VALUES(?,?,?)
	 */
	@Override
	public String getSql() {
		StringBuilder SQL = new StringBuilder();
		SQL.append("INSERT INTO ").append(tableName).append("(");
		columnNames.forEach(i -> SQL.append(i).append(","));
		SQL.deleteCharAt(SQL.length() - 1).append(")").append(PathUtils.LINE + "\t").append("VALUES").append("(");
		columnNames.forEach(i -> SQL.append("?").append(","));
		SQL.deleteCharAt(SQL.length() - 1).append(")");
		if (log.isDebugEnabled()){
			log.debug(" 生成的sql语句: " + SQL.toString());
		}
		return SQL.toString();
	}

}
