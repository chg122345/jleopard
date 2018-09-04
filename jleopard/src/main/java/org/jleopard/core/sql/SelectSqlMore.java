package org.jleopard.core.sql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
public class SelectSqlMore implements Sql {

	private static final Log log = LogFactory.getLog(SelectSqlMore.class);

	private String tableName; // 表名

	private List<String> allColumnNames; // 所有字段名

	private String primaryKeyName;

	public SelectSqlMore(Class<?> cls) {
		List<String> allcolumns = new ArrayList<>();
		this.tableName = TableUtil.getTableName(cls);
		this.primaryKeyName = FieldUtil.getPrimaryKeys(cls).get(0);
		Set<String> set = FieldUtil.getAllColumnName(cls);
		Map<String, Field> columnField = FieldUtil.getAllColumnName_Field(cls);
		columnField.forEach((c, f) -> {
			if (Collection.class.isAssignableFrom(f.getType())) {
				ParameterizedType pm = (ParameterizedType) f.getGenericType();
				Class<?> fcls = (Class<?>) pm.getActualTypeArguments()[0];
				if (TableUtil.isTable(fcls)) {
					set.remove(c);
				}
			}
		});
		set.stream().forEach(column -> allcolumns.add(column));
		this.allColumnNames = allcolumns;

	}

	/**
	 *
	 * @return SELECT ID,NAME,PHONE,ADDRESS FROM USER
	 *
	 */
	@Override
	public String getSql() {
		StringBuilder SQL = new StringBuilder();
		SQL.append("SELECT ");
		for (String columnname : allColumnNames) {
			SQL.append(columnname).append(",");
		}
		SQL.deleteCharAt(SQL.length() - 1).append(" ").append("FROM").append(" ").append(tableName)
				.append(PathUtils.LINE);
		log.info(" 生成的sql语句: " + SQL.toString());
		return SQL.toString();
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

}
