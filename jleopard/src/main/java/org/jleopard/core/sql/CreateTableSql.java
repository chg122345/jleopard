package org.jleopard.core.sql;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import org.jleopard.core.annotation.Column;
import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.JavaTypeUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.exception.SessionException;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.session.sessionFactory.ColumnNameHelper;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.PathUtils;
import org.jleopard.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/11
 * <p>
 * Find a way for success and not make excuses for failure. 
 * <p>
 * 生成建表的sql语句
 */
public class CreateTableSql implements Sql {

	private static final Log log = LogFactory.getLog(CreateTableSql.class);

	private Class<?> cls;

	private String tableName; // 表名

	public CreateTableSql(Class<?> cls) {
		this.cls = cls;
		this.tableName = TableUtil.getTableName(cls);
		if (StringUtil.isEmpty(tableName)) {
			log.error(cls + " 无 @Table注解");
			throw new SessionException("["+ cls + "] 无@Table注解...");
		}

	}

	/**
	 * 自动建表sql语句
	 *
	 * @return
	 */
	@Override
	public String getSql() {
		StringBuilder SQL = new StringBuilder();
		SQL.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(PathUtils.LINE).append(" (");
		StringBuilder endSql = new StringBuilder();
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			if (Collection.class.isAssignableFrom(field.getType())) {
				ParameterizedType pm = (ParameterizedType) field.getGenericType();
				Class<?> fcls = (Class<?>) pm.getActualTypeArguments()[0];
				if (TableUtil.isTable(fcls)) {
					continue;
				}
			}
			Column column = field.getDeclaredAnnotation(Column.class);
			if (FieldUtil.isPrimaryKey(column) == 0) {
				String foreignKeyName = ColumnNameHelper.getColumnName(field); // 外键名
				Class<?> clazz = column.join(); // 外键类
				if (clazz != Object.class) {
					String fpkName = CollectionUtil.isNotEmpty(FieldUtil.getPrimaryKeys(clazz))
							? FieldUtil.getPrimaryKeys(clazz).get(0)
							: null; // 外键类的主键名
					Class<?> fpkType = FieldUtil.getPrimaryKeys_Type(clazz).get(fpkName); // 外键类的主键的类型
					SQL.append(foreignKeyName).append(" ").append(JavaTypeUtil.getSqlType(fpkType)).append(" ")
							.append("NOT NULL").append(",").append(PathUtils.LINE);
					// endSql="key ("+foreignKeyName+")";
					String joinSql = "CONSTRAINT FOREIGN KEY(" + foreignKeyName + ") REFERENCES "
							+ TableUtil.getTableName(clazz) + "(" + fpkName
							+ ") ON DELETE CASCADE ON UPDATE CASCADE, \n";
					endSql.append(joinSql);
				} else {
					if (column.allowNull()) {
						SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
								.append(JavaTypeUtil.getSqlType(field.getType())).append(",").append(PathUtils.LINE);
					} else {
						SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
								.append(JavaTypeUtil.getSqlType(field.getType())).append(" ").append("NOT NULL")
								.append(",").append(PathUtils.LINE);
					}
				}

			} else if (FieldUtil.isPrimaryKey(column) == 1 || FieldUtil.isPrimaryKey(column) == 3) {
				SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
						.append(JavaTypeUtil.getSqlType(field.getType())).append(" ").append("PRIMARY KEY").append(" ")
						.append("NOT NULL").append(",").append(PathUtils.LINE);
			} else if (FieldUtil.isPrimaryKey(column) == 2) {
				SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
						.append(JavaTypeUtil.getSqlType(field.getType())).append(" ").append("PRIMARY KEY").append(" ")
						.append("auto_increment").append(" ").append("NOT NULL").append(",").append(PathUtils.LINE);
			}

		}
		// SQL.deleteCharAt(SQL.length()-2).append(")");
		if (StringUtil.isEmpty(endSql.toString())) {
			SQL.deleteCharAt(SQL.length() - 2).append(")");
		} else {
			SQL.append(endSql.deleteCharAt(endSql.length() - 3)).append(")");
		}

		return SQL.toString();
	}
}
