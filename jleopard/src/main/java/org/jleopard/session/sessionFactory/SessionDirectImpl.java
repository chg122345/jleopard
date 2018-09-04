package org.jleopard.session.sessionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.jleopard.core.sql.CreateTableSql;
import org.jleopard.core.sql.DeleteSql;
import org.jleopard.core.sql.DeleteSqlMore;
import org.jleopard.core.sql.InsertSql;
import org.jleopard.core.sql.JoinSql;
import org.jleopard.core.sql.SelectSql;
import org.jleopard.core.sql.SelectSqlMore;
import org.jleopard.core.sql.Sql;
import org.jleopard.core.sql.UpdateSql;
import org.jleopard.core.util.FieldUtil;
import org.jleopard.core.util.TableUtil;
import org.jleopard.exception.NotFoundFieldException;
import org.jleopard.exception.SessionException;
import org.jleopard.exception.SqlSessionException;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.pageHelper.PageInfo;
import org.jleopard.session.Configuration;
import org.jleopard.session.SqlSession;
import org.jleopard.util.ArrayUtil;
import org.jleopard.util.ClassUtil;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.PathUtils;
import org.jleopard.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/10
 * <p>
 * Find a way for success and not make excuses for failure.
 * <p>
 * 核心部分 分别封装对数据库的增删改查 分页查询信息
 */
final class SessionDirectImpl implements SqlSession {

	private static final Log LOG = LogFactory.getLog(SessionDirectImpl.class);
	private Configuration configuration;
	private DataSource dataSource;
	private Connection conn;
	private PreparedStatement pstm;
	private Statement stm;
	private ResultSet res;
	private boolean DevModel;

	private void open() {
		if (conn == null) {
			LOG.error(" 获取数据库连接失败....");
			throw new SessionException("获取数据库连接失败... or session 已经关闭...");
		} else {
			try {
				if (!configuration.isAutoCommit()) {
					conn.setAutoCommit(false);
				}
			} catch (SQLException e) {
				throw new SessionException("设置事物出错了...", e);
			}
		}

	}

	/**
	 * 初始化时建立实体对象对应的数据库表 当前表名在数据库中已存在时 不会新建表
	 */
	public SessionDirectImpl(Configuration configuration) {
		this.configuration = configuration;
		this.dataSource = configuration.getDataSource();
		this.DevModel = configuration.isDev();
		try {
			this.conn = dataSource.getConnection();
		} catch (SQLException e1) {
			LOG.error("获取数据库连接失败...");
			throw new SessionException("获取数据库连接失败...", e1);
		}
		Set<Class<?>> set = ClassUtil.getClassSetByPackagename(configuration.getEntityPackage());
		List<String> list = TableUtil.showAllTableName(conn);
		if (CollectionUtil.isEmpty(set)) {
			LOG.error("获取到的实体类为空...");
			throw new SessionException("获取到的实体类为空...");
		}
		set.stream().forEach(cls -> {
			if (!list.contains(TableUtil.getTableName(cls))) {
				Sql create = new CreateTableSql(cls);
				String sql = create.getSql();
				try {
					stm = conn.createStatement();
					stm.executeUpdate(sql);
				} catch (SQLException e) {
					LOG.error(cls + "创建表时出错...", e);
					e.printStackTrace();
				}
			}

		});
	}

	/**
	 * insert 对象到数据库
	 *
	 * @param entity
	 *            信息封装完成的对象
	 * @return 数据库变化的 row 数
	 * @throws SqlSessionException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> int Save(T entity) throws SqlSessionException {
		this.open();
		InsertSql insertsql = new InsertSql<T>(entity);
		String sql = insertsql.getSql();
		List values = insertsql.getValues();
		LOG.info("当前执行的sql语句: \n \t" + sql + "Paramter: " + values + PathUtils.LINE);
		try {
			pstm = conn.prepareStatement(sql);
			pstmSetListValues(pstm, values);
			return pstm.executeUpdate();
		} catch (SQLException e) {
			throw new SqlSessionException(" sql执行出错了...", e);
		}
	}

	/**
	 * 一次添加多个对象进数据库
	 *
	 * @param list
	 *            把要操作的对象添用一个list封装完整
	 * @return 数据库变化的 row 数
	 * @throws SqlSessionException
	 */
	@Override
	public <T> int SaveMore(List<T> list) throws SqlSessionException {
		if (CollectionUtil.isEmpty(list)) {
			LOG.warn(" 传入的 list 对象为空...");
			return 0;
		}
		int result = 0;
		for (Object object : list) {
			int temp = this.Save(object);
			if (temp > 0) {
				result++;
			}
		}
		return result;
	}

	/**
	 * 执行自定义sql ( 注 : 不能是查询语句 不能是查询语句 不能是查询语句) (此处只能是 更新 操作的sql语句...)
	 *
	 * @param sql
	 *            动态sql 例: select * from user where id=? and name=?
	 * @param args
	 *            动态sql的参数 一定要按顺序传递
	 * @return 数据库变化的 row 数
	 * @throws SqlSessionException
	 */
	@Override
	public int MySql(String sql, Object... args) throws SqlSessionException {
		this.open();
		LOG.info("当前执行的sql语句: \n \t" + sql + "Paramter: " + args + PathUtils.LINE);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			return pstm.executeUpdate();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了...", e);
		}
	}

	/**
	 * 删除数据库信息操作 把要执行删除的条件封装为一个对象
	 *
	 * @param entity
	 *            封装号信息的对象
	 * @return 数据库变化的 row 数
	 * @throws SqlSessionException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> int Delete(T entity) throws SqlSessionException {
		this.open();
		DeleteSql deletesql = new DeleteSql<T>(entity);
		List values = deletesql.getValues();
		String sql = deletesql.getSql();
		LOG.info("当前执行的sql语句: \n \t" + sql + "Paramter: " + values + PathUtils.LINE);
		try {
			pstm = conn.prepareStatement(sql);
			pstmSetListValues(pstm, values);
			return pstm.executeUpdate();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了... ", e);
		}
	}

	/**
	 * 根据唯一约束主键删除数据
	 *
	 * @param cls
	 *            要删除的对象的类
	 * @param primaryKeys
	 *            唯一主键( 可传多个值(批量删除))
	 * @return 数据库变化的 row 数
	 * @throws SqlSessionException
	 */
	@Override
	public <T> int Delete(Class<T> cls, Object... primaryKeys) throws SqlSessionException {
		this.open();
		DeleteSqlMore deletesql = new DeleteSqlMore(cls);
		StringBuilder SQL = new StringBuilder();
		SQL.append(deletesql.getSql()).append(" ").append(ArraysHelper.getSql(primaryKeys));
		String sql = SQL.toString();
		DevModelHelper.outParameter(DevModel, sql, primaryKeys);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(primaryKeys)) {
				pstmSetArrayValues(pstm, primaryKeys);
			}
			return pstm.executeUpdate();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了... ", e);
		}
	}

	/*
	 * @Override public int Update(Object entity) throws SQLException { this.open();
	 * Sql updatesql=new UpdateSql(entity); String sql=updatesql.getSql(); List
	 * values=updatesql.getValues(); LOG.info("当前执行的sql语句: \n" +sql);
	 * pstm=conn.prepareStatement(sql); pstmSetListValues(pstm,values);
	 * outListParameter(sql,values); return pstm.executeUpdate(); }
	 */

	/**
	 * 根据唯一主键更新数据 目标数据封装进完整对象 ( 主键可有可无, 此处不在该对象中取主键值(即主键默认不可更改))
	 *
	 * @param entity
	 *            封装好的对象
	 * @param primaryKey
	 *            唯一主键值
	 * @return 数据库变化的 row 数
	 * @throws SqlSessionException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> int Update(T entity, Object primaryKey) throws SqlSessionException {
		this.open();
		UpdateSql updatesql = new UpdateSql(entity);
		List pks = FieldUtil.getPrimaryKeys(entity.getClass());
		if (CollectionUtil.isEmpty(pks)) {
			LOG.error(entity + " 没有找到唯一标识主键...");
			throw new NotFoundFieldException(entity + " 没有找到唯一标识主键...");
		}
		StringBuilder SQL = new StringBuilder();
		SQL.append(updatesql.getSql()).append(pks.get(0)).append("=").append("?");
		String sql = SQL.toString();
		List values = updatesql.getValues();
		DevModelHelper.outParameter(DevModel, sql, values);
		try {
			pstm = conn.prepareStatement(sql);
			pstmSetListValues(pstm, values);
			pstm.setObject(values.size() + 1, primaryKey);
			values.add(primaryKey);
			return pstm.executeUpdate();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了... ", e);
		}
	}

	/**
	 * 自定义查询sql
	 *
	 * @param sql
	 *            自定义查询sql
	 * @param args
	 *            动态参数
	 * @return 从数据库查询出的结果集
	 * @throws SqlSessionException
	 */
	@Override
	public ResultSet Get(String sql, Object... args) throws SqlSessionException {
		this.open();
		DevModelHelper.outParameter(DevModel, sql, args);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			res = pstm.executeQuery();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了... ", e);
		}
		return res;
	}

	/**
	 * 多表链接查询 返回有外键的对象 m 2 one 返回m 对象 one对象放置m 对象中
	 *
	 * @param cls1
	 *            m对象类 （返回的对象类）
	 * @param cls2
	 *            one对象类
	 * @param where
	 *            查询条件 不加条件留空 "" 后面参数也必须留空 ""
	 * @param args
	 *            查询条件的动态参数 按顺序依次写入
	 * @param <T>
	 * @return
	 * @throws SqlSessionException
	 */
	@Override
	public <T> List<T> Get(Class<T> cls1, Class<?>[] clazz, String where, Object... args) throws SqlSessionException {
		this.open();
		Sql joinSql = new JoinSql(cls1, clazz);
		StringBuilder SQL = new StringBuilder();
		if (StringUtil.isEmpty(where)) {
			SQL.append(joinSql.getSql());
		} else {
			if (where.toLowerCase().startsWith("where")) {
				SQL.append(joinSql.getSql()).append(PathUtils.LINE).append(where);
			} else {
				SQL.append(joinSql.getSql()).append(PathUtils.LINE).append(" WHERE").append(" ").append(where);
			}
		}
		String sql = SQL.toString();
		// LOG.info("当前执行的sql语句: \n \t" + sql + where + "Paramter: " + args +
		// PathUtils.LINE);
		DevModelHelper.outParameter(DevModel, sql, args);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			res = pstm.executeQuery();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了....", e);
		}
		return EntityHelper.invoke(res, this, cls1, clazz);
	}

	/**
	 * 查询指定对象类 自定义查询条件 动态sql 拼装后的sql 例: SELECT ID,NAME,PHONE,ADDRESS FROM USER WHERE
	 * ID>？ AND ID<？ ORDER BY ID DESC 按顺序传参
	 *
	 * @param cls
	 *            查询指定对象类
	 * @param where
	 *            sql 后续条件: (where(可有可无,处理时默认去掉自带where关键字)) id>10086 and id<10089
	 *            order by id desc
	 * @return 查出的结果 list 形式
	 * @throws SqlSessionException
	 */
	@Override
	public <T> List<T> Get(Class<T> cls, String where, Object... args) throws SqlSessionException {
		this.open();
		Sql selectsql = new SelectSqlMore(cls);
		StringBuilder SQL = new StringBuilder();
		if (where.toLowerCase().startsWith("where")) {
			SQL.append(selectsql.getSql()).append(PathUtils.LINE).append(where);
		} else {
			SQL.append(selectsql.getSql()).append(PathUtils.LINE).append(" WHERE").append(" ").append(where);
		}
		String sql = SQL.toString();
		// LOG.info("当前执行的sql语句: \n \t" + sql + where + "Paramter: " + args +
		// PathUtils.LINE);
		DevModelHelper.outParameter(DevModel, sql, args);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			res = pstm.executeQuery();
		} catch (SQLException e) {
			throw new SqlSessionException(" sql执行出错了... ", e);
		}
		Map<String, String> C_F = FieldUtil.getColumnFieldName(cls);

		return EntityHelper.invoke(res, cls, C_F);

	}

	/**
	 * 对查询单个对象的操作 把条件封装为一个完整实体对象
	 *
	 * @param entity
	 * @param <T>
	 *            泛型 传一个什么对象 就返回一个什么对象类型
	 * @return 查出的单个对象(如果该对象条件符合多条数据 ， 则只返回第一条数据)
	 * @throws SqlSessionException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public <T> List<T> Get(T entity) throws SqlSessionException {
		this.open();
		SelectSql selectsql = new SelectSql(entity);
		String sql = selectsql.getSql();
		// sql = sql + " LIMIT 1";
		List<Object> values = selectsql.getValues();
		DevModelHelper.outParameter(DevModel, sql, values);
		try {
			pstm = conn.prepareStatement(sql);
			pstmSetListValues(pstm, values);
			res = pstm.executeQuery();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了... ", e);
		}

		Map<String, String> C_F = FieldUtil.getColumnFieldName(entity.getClass());
		List list = null;

		list = EntityHelper.invoke(res, entity.getClass(), C_F);

		if (CollectionUtil.isEmpty(list)) {
			return null;
		} else {
			return list;
		}

	}

	/**
	 * 根据唯一主键查询 返回一个或多个对象(由你带的参数决定 1 or m)
	 *
	 * @param cls
	 *            要返回的对象类
	 * @param primaryKeys
	 *            该对象的唯一主键（可传多个 查询多个） 注: 如果该对象有多个主键，则默认取第一主键作为此次查询的标识
	 * @return
	 * @throws SqlSessionException
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public <T> List<T> Get(Class<T> cls, Object... primaryKeys) throws SqlSessionException {
		this.open();
		Sql selectsql = new SelectSqlMore(cls);
		StringBuilder SQL = new StringBuilder();
		List pks = FieldUtil.getPrimaryKeys(cls);
		if (CollectionUtil.isEmpty(pks)) {
			LOG.error(cls + " 没有找到唯一标识主键...");
			throw new NotFoundFieldException(cls + " 没有找到唯一标识主键...");
		}
		SQL.append(selectsql.getSql()).append(PathUtils.LINE).append(" WHERE").append(" ").append(pks.get(0))
				.append(" ").append(ArraysHelper.getSql(primaryKeys));
		String sql = SQL.toString();
		DevModelHelper.outParameter(DevModel, sql, primaryKeys);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(primaryKeys)) {
				pstmSetArrayValues(pstm, primaryKeys);
			}
			res = pstm.executeQuery();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了...", e);
		}
		Map<String, String> C_F = FieldUtil.getColumnFieldName(cls);

		return EntityHelper.invoke(res, cls, C_F);

	}

	/**
	 * 从数据库查询所有该类对象 以 list 形式返回
	 *
	 * @param cls
	 *            要查询对象的实体类
	 * @return list
	 * @throws SessionException
	 */
	@Override
	public <T> List<T> Get(Class<T> cls) throws SqlSessionException {
		this.open();
		Sql selectsql = new SelectSqlMore(cls);
		Map<String, String> C_F = FieldUtil.getColumnFieldName(cls);
		String sql = selectsql.getSql();
		// LOG.info("当前执行的sql语句: \n" + sql + PathUtils.LINE);
		DevModelHelper.outParameter(DevModel, sql, "");
		try {
			stm = conn.createStatement();
			res = stm.executeQuery(sql);
		} catch (SQLException e) {
			throw new SqlSessionException(" sql执行出错了... ", e);
		}

		return EntityHelper.invoke(res, cls, C_F);

	}

	@Override
	public <T> List<T> GetOne2Many(Class<T> cls1, Class<?>[] clazz, String where, Object... args)
			throws SqlSessionException {
		this.open();
		Sql joinSql = new JoinSql(cls1, clazz);
		StringBuilder SQL = new StringBuilder();
		if (StringUtil.isEmpty(where)) {
			SQL.append(joinSql.getSql());
		} else {
			if (where.toLowerCase().startsWith("where")) {
				SQL.append(joinSql.getSql()).append(PathUtils.LINE).append(where);
			} else {
				SQL.append(joinSql.getSql()).append(PathUtils.LINE).append(" WHERE").append(" ").append(where);
			}
		}
		String sql = SQL.toString();
		DevModelHelper.outParameter(DevModel, sql, args);
		try {
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			res = pstm.executeQuery();
		} catch (SQLException e) {
			throw new SqlSessionException("sql执行出错了....", e);
		}
		return EntityHelper.invoke(res, this, cls1, clazz);

	}

	/**
	 * 主流的分页查询 (仅支持mysql)
	 *
	 * @param cls
	 *            要查询的对象的实体类
	 * @param page
	 *            查询第几页 start 1
	 * @param pageSize
	 *            每页显示多少条数据
	 * @return PageInfo类(分页信息)
	 * @throws Exception
	 */
	@Override
	public <T> PageInfo GetToPage(Class<T> cls, int page, int pageSize, String where, Object... args)
			throws SqlSessionException {
		this.open();
		Sql selectsql = new SelectSqlMore(cls);
		Map<String, String> C_F = FieldUtil.getColumnFieldName(cls);
		try {
			stm = conn.createStatement();
			String csql = "SELECT COUNT(*) FROM " + TableUtil.getTableName(cls);
			res = stm.executeQuery(csql);
			int total = 0;
			while (res.next()) {
				total = res.getInt(1);
			}
			PageInfo pageInfo = new PageInfo(total, pageSize);
			StringBuilder SQL = new StringBuilder();
			if (page <= 1) {
				page = 1;
			} else if (page >= pageInfo.getTotalPages()) {
				page = pageInfo.getTotalPages();
			}
			pageInfo.setPage(page);
			int star = (page - 1) * pageSize;
			if (where.toLowerCase().startsWith("where")) {
				SQL.append(selectsql.getSql()).append(PathUtils.LINE).append(where);
			} else {
				SQL.append(selectsql.getSql()).append(PathUtils.LINE).append(" WHERE").append(" ").append(where);
			}
			SQL.append(" ").append("LIMIT").append(" ").append(star).append(",").append(pageSize);
			String sql = SQL.toString();
			DevModelHelper.outParameter(DevModel, sql, args);
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			res = pstm.executeQuery();
			pageInfo.setList(EntityHelper.invoke(res, cls, C_F));
			return pageInfo;
		} catch (SQLException e) {
			throw new SqlSessionException(" sql执行出错了... ", e);
		}

	}

	@Override
	public <T> PageInfo GetToPage(Class<T> cls1, Class<?>[] clazz, int page, int pageSize, String where, Object... args)
			throws SqlSessionException {
		this.open();
		Sql joinSql = new JoinSql(cls1, clazz);
		try {
			stm = conn.createStatement();
			String csql = "SELECT COUNT(*) FROM " + TableUtil.getTableName(cls1);
			res = stm.executeQuery(csql);
			int total = 0;
			while (res.next()) {
				total = res.getInt(1);
			}
			PageInfo pageInfo = new PageInfo(total, pageSize);
			StringBuilder SQL = new StringBuilder();
			if (page <= 1) {
				page = 1;
			} else if (page >= pageInfo.getTotalPages()) {
				page = pageInfo.getTotalPages();
			}
			pageInfo.setPage(page);
			int star = (page - 1) * pageSize;
			if (where.toLowerCase().startsWith("where")) {
				SQL.append(joinSql.getSql()).append(PathUtils.LINE).append(where);
			} else {
				SQL.append(joinSql.getSql()).append(PathUtils.LINE).append(" WHERE").append(" ").append(where);
			}
			SQL.append(" ").append("LIMIT").append(" ").append(star).append(",").append(pageSize);
			String sql = SQL.toString();
			DevModelHelper.outParameter(DevModel, sql, args);
			pstm = conn.prepareStatement(sql);
			if (ArrayUtil.isNotEmpty(args)) {
				pstmSetArrayValues(pstm, args);
			}
			res = pstm.executeQuery();
			pageInfo.setList(EntityHelper.invoke(res, this, cls1, clazz));
			return pageInfo;
		} catch (SQLException e) {
			throw new SqlSessionException(" sql执行出错了... ", e);
		}

	}

	@Override
	public void Commit() throws SqlSessionException {
		try {
			conn.commit();
		} catch (SQLException e) {
			throw new SqlSessionException("事务提交出错了...", e);
		}
	}

	@Override
	public void Rollback() throws SqlSessionException {
		try {
			conn.rollback();
		} catch (SQLException e) {
			throw new SqlSessionException("事物回滚出错了...", e);
		}
	}

	/**
	 * 关闭数据库操作 释放资源
	 *
	 * @throws SqlSessionException
	 */
	@Override
	public void Stop() throws SqlSessionException {
		try {
			if (res != null) {
				res.close();
			}
			if (stm != null) {
				stm.close();
			}
			if (pstm != null) {
				pstm.close();
			}
		} catch (SQLException e) {
			throw new SqlSessionException("暂停数据库连接出错了... ", e);
		}

	}

	@Override
	public void Close() throws SqlSessionException {

		try {
			if (res != null) {
				res.close();
			}
			if (stm != null) {
				stm.close();
			}
			if (pstm != null) {
				pstm.close();
			}
			if (conn != null) {
				conn.close();
				this.conn = null;
			}
		} catch (SQLException e) {
			throw new SqlSessionException("关闭数据库连接出错了... ", e);
		}
	}

	/**
	 * ------------------------处理动态参数的方法--------------------------------------------
	 **/
	@SuppressWarnings("rawtypes")
	private void pstmSetListValues(PreparedStatement pstm, List values) throws SQLException {
		for (int i = 0; i < values.size(); ++i) {
			pstm.setObject(i + 1, values.get(i));
		}
	}

	private void pstmSetArrayValues(PreparedStatement pstm, Object[] args) throws SQLException {
		for (int i = 0; i < args.length; ++i) {
			pstm.setObject(i + 1, args[i]);
		}
	}
}
