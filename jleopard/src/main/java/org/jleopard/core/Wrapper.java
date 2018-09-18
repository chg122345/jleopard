/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-09-18  上午9:48
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.core;

import org.jleopard.util.ArrayUtil;
import org.jleopard.util.StringUtil;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

/**
 *
 * @version 2.3.0
 * 条件构造器
 */
public abstract class Wrapper<T> implements Serializable {
    private static final String WHERE = " WHERE {%s}";
    private static final String GROUP_BY = " GROUP BY {%s}";
    private static final String HAVING = " HAVING {%s}";
    private static final String RDER_BY = " ORDER BY {%s}";
    protected String paramAlias = null;
    protected String sql = null;
    protected String AND_OR = "AND";

    public Wrapper() {
    }

    public String getSql() {
        return StringUtil.isEmpty(this.sql) ? null : this.stripSqlInjection(this.sql);
    }

    public Wrapper<T> setSql(String sql) {
        if (StringUtil.isNotEmpty(sql)) {
            this.sql = sql;
        }
        return this;
    }

    public Wrapper<T> setSql(String... columns) {
        StringBuilder builder = new StringBuilder();
        String[] arr$ = columns;
        int len$ = columns.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String column = arr$[i$];
            if (StringUtil.isNotEmpty(column)) {
                if (builder.length() > 0) {
                    builder.append(",");
                }

                builder.append(column);
            }
        }

        this.sql = builder.toString();
        return this;
    }

    public Wrapper<T> where(String sqlWhere, Object... params) {
        if (StringUtil.isNotEmpty(sqlWhere)) {
            if (ArrayUtil.isNotEmpty(params)){

            }
        }

        return this;
    }

    public Wrapper<T> eq(String column, Object params) {
        return this;
    }

    public Wrapper<T> ne(String column, Object params) {

        return this;
    }



    public Wrapper<T> allEq(Map<String, Object> params) {
        return this;
    }

    public Wrapper<T> gt(String column, Object params) {

        return this;
    }



    public Wrapper<T> ge(String column, Object params) {

        return this;
    }


    public Wrapper<T> lt(String column, Object params) {

        return this;
    }


    public Wrapper<T> le(String column, Object params) {

        return this;
    }


    public Wrapper<T> and(String sqlAnd, Object... params) {

        return this;
    }


    public Wrapper<T> andNew(String sqlAnd, Object... params) {
        return this;
    }


    public Wrapper<T> and() {
        this.sql += " AND";
        return this;
    }

    public Wrapper<T> or() {
        this.sql += " OR";
        return this;
    }

    public Wrapper<T> or(String sqlOr, Object... params) {


        return this;
    }



    public Wrapper<T> orNew() {
        return this;
    }

    public Wrapper<T> orNew(String sqlOr, Object... params) {

        return this;
    }


    public Wrapper<T> groupBy(String columns) {


        return this;
    }

    public Wrapper<T> having(String sqlHaving, Object... params) {


        return this;
    }


    public Wrapper<T> orderBy(String columns) {

        return this;
    }

    public Wrapper<T> orderBy(String columns, boolean isAsc) {
        return this;
    }

    public Wrapper<T> like(String column, String value) {
        return this;
    }

    public Wrapper<T> notLike(String column, String value) {
        return this;
    }


    public Wrapper<T> exists(String value) {

        return this;
    }



    public Wrapper<T> notExists(String value) {
        return this;
    }


    public Wrapper<T> in(String column, String value) {
        return this;
    }

    public Wrapper<T> notIn(String column, String value) {
        return this;
    }


    public Wrapper<T> in(String column, Collection<?> value) {
        return this;
    }

    public Wrapper<T> notIn(String column, Collection<?> value) {
        return this;
    }

    public Wrapper<T> in(String column, Object[] value) {
        return this;
    }


    public Wrapper<T> notIn(String column, Object... value) {
        return this;
    }

    public Wrapper<T> between(String column, Object val1, Object val2) {
        return this;
    }

    public Wrapper<T> notBetween(String column, Object val1, Object val2) {
        return this;
    }

    public Wrapper<T> addFilter(String sqlWhere, Object... params) {
        return this.and(sqlWhere, params);
    }

    public Wrapper<T> addFilterIfNeed(boolean need, String sqlWhere, Object... params) {
        return need ? this.where(sqlWhere, params) : this;
    }

    protected String stripSqlInjection(String value) {
        return value.replaceAll("('.+--)|(--)|(\\|)|(%7C)", "");
    }

    public Wrapper<T> last(String limit) {
        return this;
    }

}

