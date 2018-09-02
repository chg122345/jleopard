package org.jleopard.core.sql;

import java.lang.reflect.Field;

import org.jleopard.core.annotation.Column;
import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.JavaTypeUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.exception.SessionException;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.session.sessionFactory.ColumnNameHelper;
import org.jleopard.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/11
 * <p>
 * Find a way for success and not make excuses for failure. 生成建表的sql语句
 */
public class CreateTableSql implements Sql {

	private static final Log LOG = LogFactory.getLog(CreateTableSql.class);

	private Class<?> cls;

	private String tableName; // 表名

	public CreateTableSql(Class<?> cls) {
		this.cls = cls;
		this.tableName = TableUtil.getTableName(cls);
		if (StringUtil.isEmpty(tableName)) {
			LOG.error(cls + " 无 @Table注解");
			throw new SessionException(cls + " 无@Table注解...");
		}

	}

	/**
     *   自动建表sql语句
     *
     * @return
     */
    @Override
	public String getSql() {
		StringBuilder SQL = new StringBuilder();
		SQL.append("create table if not exists ").append(tableName).append("\n").append(" (");
		String endSql = null;
		Field[] fields = cls.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getDeclaredAnnotation(Column.class);
			if (FieldUtil.isPrimaryKey(column) == 0) {
				String foreignKeyName = ColumnNameHelper.getColumnName(field); // 外键名
				Class<?> clazz = column.relation(); // 外键类
				if (clazz != Object.class) {
					String fpkName = FieldUtil.getPrimaryKeys(clazz).get(0); // 外键类的主键名
					Class<?> fpkType = FieldUtil.getPrimaryKeys_Type(clazz).get(fpkName); // 外键类的主键的类型
					SQL.append(foreignKeyName).append(" ").append(JavaTypeUtil.getSqlType(fpkType)).append(" ")
							.append("not null").append(",").append("\n");
					// endSql="key ("+foreignKeyName+")";
					endSql = "constraint foreign key(" + foreignKeyName + ") references "
							+ TableUtil.getTableName(clazz) + "(" + fpkName + ") on delete cascade on update cascade";
				} else {
					if (column.allowNull()) {
						SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
								.append(JavaTypeUtil.getSqlType(field.getType())).append(",").append("\n");
					} else {
						SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
								.append(JavaTypeUtil.getSqlType(field.getType())).append(" ").append("not null")
								.append(",").append("\n");
					}
				}

			} else if (FieldUtil.isPrimaryKey(column) == 1) {
				SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
						.append(JavaTypeUtil.getSqlType(field.getType())).append(" ").append("primary key").append(" ")
						.append("not null").append(",").append("\n");
			} else if (FieldUtil.isPrimaryKey(column) == 2) {
				SQL.append(ColumnNameHelper.getColumnName(field)).append(" ")
						.append(JavaTypeUtil.getSqlType(field.getType())).append(" ").append("primary key").append(" ")
						.append("auto_increment").append(" ").append("not null").append(",").append("\n");
			}

		}
		// SQL.deleteCharAt(SQL.length()-2).append(")");
		if (StringUtil.isEmpty(endSql)) {
			SQL.deleteCharAt(SQL.length() - 2).append(")");
		} else {
			SQL.append(endSql).append("\n").append(")");
		}

		return SQL.toString();
	}
}
