package org.jleopard.core.sql;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.PathUtils;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/19 update 2018/9/3
 * <p>
 * Find a way for success and not make excuses for failure.
 * <p/>
 * <p>
 * 生成关联查询的sql语句
 */
public class JoinSql implements Sql, CloumnNames {

    private static final Log LOG = LogFactory.getLog(JoinSql.class);

    private String tableName1; // 表名1

    private List<String> allColumnNames; // 所有字段名

    private Map<Map<String, String>, Map<String, Set<String>>> t2ColumnNames; // ((目标对象的外键名 表名) (主键 所有字段名))

    public JoinSql(Class<?> cls1, Class<?>... clazz) {
        List<String> allColumns = new ArrayList<>();
        Map<Map<String, String>, Map<String, Set<String>>> t2Columns = new HashMap<>();
        this.tableName1 = TableUtil.getTableName(cls1);
        Map<String, Class<?>> fMap = FieldUtil.getForeignKeys(cls1);
        Set<String> set = FieldUtil.getAllColumnName(cls1);
        for (Class<?> cls2 : clazz) {
            fMap.forEach((k, v) -> {
                if (v == cls2) {
                    String pk = CollectionUtil.isNotEmpty(FieldUtil.getPrimaryKeys(cls2))
                            ? FieldUtil.getPrimaryKeys(cls2).get(0)
                            : null;
                    Set<String> set2 = FieldUtil.getAllColumnName(cls2);
                    Map<String, String> ft = new HashMap<>();
                    ft.put(k, TableUtil.getTableName(cls2));
                    Map<String, Set<String>> fC = new HashMap<>();
                    fC.put(pk, set2);
                    t2Columns.put(ft, fC);
                }
            });
        }
		/*List<String> fName = FieldUtil.getForeignKeyName(cls1);
		fName.stream().forEach(i -> set.remove(i));*/
        allColumns.addAll(set);
        this.allColumnNames = allColumns;
        this.t2ColumnNames = t2Columns;
    }

    @Override
    public List<String> getColumnNames() {
        return allColumnNames;
    }

    /**
     * SELECT article.id,article.title,article.status,reply.id,reply.content,reply.user_id,user.id,user.name,user.password FROM article
     * LEFT JOIN reply ON article.reply_id=reply.id
     * LEFT JOIN user ON article.user_id=user.id
     */
    @Override
    public String getSql() {

        StringBuilder COL = new StringBuilder();
        // 自己本身的字段
        for (int i = 0, j = allColumnNames.size(); i < j; ++i) {
            if (i > 0) {
                COL.append(",");
            }
            COL.append(tableName1).append(".").append(allColumnNames.get(i));
        }

        StringBuilder JOIN = new StringBuilder();
        // 关联表的信息 k(目标外键名 表名) v(主键,所有字段)
        t2ColumnNames.forEach((k, v) -> {
            k.forEach((fk, t) -> {
                v.forEach((f, s) -> {
                    JOIN.append(PathUtils.LINE).append("LEFT JOIN ").append(t).append(" ON").append(" ").append(tableName1)
                            .append(".").append(fk).append("=").append(t).append(".").append(f).append(" ");
                    s.forEach(c -> COL.append(",").append(t).append(".").append(c));
                });
            });
        });

        StringBuilder SQL = new StringBuilder();
        String sql = String.format("SELECT %s FROM " + tableName1, COL.toString());
        SQL.append(sql).append(JOIN);
        LOG.info("生成的sql语句：" + SQL.toString());
        return SQL.toString();
    }
}
