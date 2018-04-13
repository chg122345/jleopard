package com.leopardframework.generator;

import com.leopardframework.core.Config;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.util.DateUtil;
import com.leopardframework.util.FileUtil;
import com.leopardframework.util.StringUtil;

import java.sql.*;
import java.util.Date;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/13
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *  逆向工程  生成简单的JavaBean
 */
public class TableToJavaBean {

    private static final Log LOG=LogFactory.getLog(TableToJavaBean.class);

    private static final String  LINE ="\r\n";

    private static final String TAB ="\t";

    private static final String ENTITYPACKAGE=Config.getEntityPackage();

    private static final String PACKAGE=Config.getGeneratorPackage();


    public void tableToBean(Connection connection, String tableName) throws SQLException {
        String sql = "select * from " + tableName + " where 1 <> 1";
        PreparedStatement ps;
        ResultSet rs;
        ps = connection.prepareStatement(sql);
        rs = ps.executeQuery();
        ResultSetMetaData md = rs.getMetaData();
        int columnCount = md.getColumnCount();
        StringBuffer sb = new StringBuffer();
        tableName = StringUtil.firstToUpper(StringUtil.underlineToCamelhump(tableName));  //下划线转大驼峰  首字母大写
        sb.append("package " + ENTITYPACKAGE + " ;");
        sb.append(LINE);
        sb.append(LINE);
        importPackage(md, columnCount, sb);
        sb.append("import com.leopardframework.core.annotation.*;");
        sb.append(LINE);
        sb.append(LINE);
        sb.append(LINE);
        sb.append("/**");
        sb.append(LINE);
        sb.append(" *");
        sb.append(LINE);
        sb.append(" * @Copyright  ").append("(c) by Chen_9g (80588183@qq.com).");
        sb.append(LINE);
        sb.append(" * @Author  Leopard Generator");
        sb.append(LINE);
        sb.append(" * @DateTime  ").append(DateUtil.formatDatetime(new Date()));
        sb.append(LINE);
        sb.append(" */");
        sb.append(LINE);
        sb.append("@Table");
        sb.append(LINE);
        sb.append("public class " + tableName + " {");
        sb.append(LINE);
        sb.append(LINE);
        defProperty(md, columnCount, sb);
        constructor(tableName,sb);
        genSetGet(md, columnCount, sb);
        sb.append("}");
        String paths = System.getProperty("user.dir");  //工程路径
        String endPath = paths/* + "\\src\\main\\java\\"*/ +"\\"+ (PACKAGE.replace("/", "\\")).replace(".", "\\");
        FileUtil.writeFile(endPath + "\\" + tableName + ".java", sb.toString());
    }

    /**
     *   导入属性所需包
     * @param md
     * @param columnCount
     * @param sb
     * @throws SQLException
     */
    private void importPackage(ResultSetMetaData md, int columnCount, StringBuffer sb) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            String im=JavaTypeHelper.getImport(md.getColumnTypeName(i)/*+"_IMPORT"*/);
            if (im!=null) {
                sb.append(im+ ";");
                sb.append(LINE);
            }
        }
    }

    /**
     *  私有成员变量
     * @param md
     * @param columnCount
     * @param sb
     * @throws SQLException
     */
    private void defProperty(ResultSetMetaData md, int columnCount, StringBuffer sb) throws SQLException {

        for (int i = 1; i <= columnCount; i++) {
            sb.append(TAB);
            String columnName = StringUtil.underlineToCamelhump(md.getColumnName(i).toLowerCase());
            sb.append("@Column");
            sb.append(LINE);
            sb.append(TAB);
            sb.append("private " + JavaTypeHelper.getPojoType(md.getColumnTypeName(i)) + " " + columnName + ";");
            sb.append(LINE);
        }
    }

    /**
     *   无参构造方法
     * @param tableName
     * @param sb
     */
    private void constructor(String tableName,StringBuffer sb){
            sb.append(LINE);
            sb.append(TAB);
            sb.append("public " + tableName + "() {");
            sb.append(LINE);
            sb.append(TAB).append("}");
            sb.append(LINE);
    }

    /**
     *   属性生成get、 set 方法
     * @param md
     * @param columnCount
     * @param sb
     * @throws SQLException
     */
    private void genSetGet(ResultSetMetaData md, int columnCount, StringBuffer sb) throws SQLException {
        for (int i = 1; i <= columnCount; i++) {
            sb.append(TAB);
            String pojoType = JavaTypeHelper.getPojoType(md.getColumnTypeName(i));
            String columnName =StringUtil.underlineToCamelhump(md.getColumnName(i).toLowerCase());
            String getName = null;
            String setName = null;
            if (columnName.length() > 1) {
                getName = "public " + pojoType + " get" + StringUtil.firstToUpper(columnName) + "() {";
                setName = "public void set" + StringUtil.firstToUpper(columnName) + "(" + pojoType + " " + columnName + ") {";
            } else {
                getName = "public get" + columnName.toUpperCase() + "() {";
                setName = "public set" + columnName.toUpperCase() + "(" + pojoType + " " + columnName + ") {";
            }
            sb.append(LINE).append(TAB).append(getName);
            sb.append(LINE).append(TAB).append(TAB);
            sb.append("return " + columnName + ";");
            sb.append(LINE).append(TAB).append("}");
            sb.append(LINE);
            sb.append(LINE).append(TAB).append(setName);
            sb.append(LINE).append(TAB).append(TAB);
            sb.append("this." + columnName + " = " + columnName + ";");
            sb.append(LINE).append(TAB).append("}");
            sb.append(LINE);

        }
    }
}
