package org.jleopard.core.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;

import org.jleopard.core.EnumId;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.OneToMany;
import org.jleopard.exception.NotFoundFieldException;
import org.jleopard.logging.log.Log;
import org.jleopard.logging.log.LogFactory;
import org.jleopard.session.sessionFactory.ColumnNameHelper;
import org.jleopard.util.ArrayUtil;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.StringUtil;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class FieldUtil {

    private static final Log log = LogFactory.getLog(FieldUtil.class);

    /**
     * 获取 字段名 值 主用于insert (2018-9-16 根据主键类型修改主键的值的值)
     *
     * @param entity
     * @return Map<String                                                               ,                                                               Object> key :对应的字段名 value : 相对应的值
     */
    public static Map<String, Object> getColumnName_Value(Object entity) {
        Map<String, Object> c_v = new LinkedHashMap<>();
        Class<?> cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class) || Collection.class.isAssignableFrom(field.getType())) {
                continue; // 没有注解 集合属性 不是我们要的对象 ignore
            }
            Column column = field.getDeclaredAnnotation(Column.class);
            if (isPrimaryKey(column) == 2) {
                continue; // 是自增主键 不要
            }
            if (isPrimaryKey(column) == 3) {
                // UUID生成主键
                String columnName = ColumnNameHelper.getColumnName(field);
                c_v.put(columnName, UUID.randomUUID().toString().replaceAll("-", ""));
            } else {
                valueFilter(entity, c_v, cls, field);
            }
        }
        return c_v;
    }

    /**
     * 获取 字段名 值 主用于insert delete (2018-4-18 添加外键的值)
     *
     * @param entity
     * @return Map<String                                                               ,                                                               Object> key :对应的字段名 value : 相对应的值
     */
    public static Map<String, Object> getAllColumnName_Value(Object entity) {
        Map<String, Object> c_v = new LinkedHashMap<>();
        Class<?> cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class) || Collection.class.isAssignableFrom(field.getType())) {
                continue; // 没有注解 集合属性 不是我们要的对象 ignore
            }
            valueFilter(entity, c_v, cls, field);
        }
        return c_v;
    }

    /**
     * 过滤空值 0
     *
     * @param entity
     * @param c_v
     * @param cls
     * @param field
     */
    private static void valueFilter(Object entity, Map<String, Object> c_v, Class<?> cls, Field field) {
        Object fieldValue;
        String columnName;
        try {
            PropertyDescriptor pd = new PropertyDescriptor(field.getName(), cls);
            Method method = pd.getReadMethod();
            if (TableUtil.isTable(field.getType())) { // 判断是否为我们所需的对象类
                fieldValue = method.invoke(entity);
                if (fieldValue == null) {
                    return;
                } else {
                    String fname = CollectionUtil.isNotEmpty(FieldUtil.getPrimaryKeys(field.getType()))
                            ? FieldUtil.getPrimaryKeys(field.getType()).get(0)
                            : null;
                    fieldValue = getAllColumnName_Value(fieldValue).get(fname); // 外连接表的主键值
                }
            } else {
                fieldValue = method.invoke(entity);
            }
            if (!isEmpty(fieldValue)) {
                columnName = ColumnNameHelper.getColumnName(field);
                // System.out.println(columnName+" ");
                c_v.put(columnName, fieldValue); // 取到我们需要的打包
            }
        } catch (Exception e) {
            log.error("getAllFieldName_Value  获取值失败..." + e);
            throw new RuntimeException("getAllFieldName_Value  获取值失败..." + e);
        }
    }

    private static boolean isEmpty(Object obj) {
        Class<?> clazz = obj.getClass();
        if (null == obj) {
            return true;
        }
        if ((clazz == int.class || clazz == byte.class || clazz == short.class) && 0 == (int) obj) {
            return true;
        }
        if (clazz == long.class && 0L == (long) obj) {
            return true;
        }
        if (clazz == float.class && 0.0f == (float) obj) {
            return true;
        }
        if (clazz == double.class && 0.0d == (double) obj) {
            return true;
        }
        return false;
    }

    /**
     * 除了主键 其余有值的都打包带走 主用于 update
     *
     * @param entity
     * @return
     */
    public static Map<String, Object> getExceptPK_ColumnName_Value(Object entity) {
        Map<String, Object> c_v = new LinkedHashMap<>();
        Class<?> cls = entity.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class) || Collection.class.isAssignableFrom(field.getType())) {
                continue; // 没有注解 不是我们要的对象 ignore
            }
            Column column = field.getDeclaredAnnotation(Column.class);
            if (isPrimaryKey(column) > 0) {
                continue; // 是主键 不要
            }
            valueFilter(entity, c_v, cls, field);
        }
        return c_v;
    }

    /**
     * 获取所有的字段名 主用于 crtateTable select
     *
     * @param cls
     * @return
     */
    public static Set<String> getAllColumnName(Class<?> cls) {
        Set<String> columns = new LinkedHashSet<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue; // 没有注解 不是我们要的对象 ignore
            }
            columns.add(ColumnNameHelper.getColumnName(field));
        }

        return columns;
    }

    public static Set<String> getAllColumnName(Object entity) {
        return getAllColumnName(entity.getClass());
    }

    /**
     * 获取对应的数据库字段名 对应的Java类型
     *
     * @param cls
     * @return Map ( key : 对应的数据库字段名 value : 对应的Java类型 )
     */
    public static Map<String, Class<?>> getAllColumnName_JavaType(Class<?> cls) {
        Map<String, Class<?>> colName_Type = new LinkedHashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue; // 没有注解 不是我们要的对象 ignore
            }
            colName_Type.put(ColumnNameHelper.getColumnName(field), field.getType());
        }

        return colName_Type;
    }

    /**
     * 获取 对应的数据库名 对应的属性Field
     *
     * @param cls
     * @return Map ( key : 对应的数据库字段名 value : 对应的属性Field )
     */
    public static Map<String, Field> getAllColumnName_Field(Class<?> cls) {
        Map<String, Field> colName_Field = new LinkedHashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue; // 没有注解 不是我们要的对象 ignore
            }
            colName_Field.put(ColumnNameHelper.getColumnName(field), field);
        }

        return colName_Field;
    }

    /**
     * 主要用于select 对对象变量赋予数据库查出的值
     *
     * @param cls 实体对象类
     * @return Map key=数据库的字段名(@Column注解上的的值)，value=实体对象的成员变量名
     */
    public static Map<String, String> getColumnFieldName(Class<?> cls) {
        Map<String, String> colNames = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        if (ArrayUtil.isEmpty(fields)) {
            log.error(cls.getName() + "没有成员变量...");
            throw new NotFoundFieldException(cls + "没有成员变量...");
        }
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            String fieldName = field.getName();
            String colName = ColumnNameHelper.getColumnName(field);
            colNames.put(colName, fieldName);

        }
        return colNames;
    }

    /**
     * 获取所有主键
     *
     * @param cls
     * @return list
     */
    public static List<String> getPrimaryKeys(Class<?> cls) {
        List<String> pks = new ArrayList<String>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getDeclaredAnnotation(Column.class);
            if (isPrimaryKey(column) > 0) {
                pks.add(ColumnNameHelper.getColumnName(field));
            }
        }
        return pks;
    }

    /**
     * 获取主键列名 类型 key: 主键列名 value : 主键对应的类型
     *
     * @param cls
     * @return map
     */
    public static Map<String, Class<?>> getPrimaryKeys_Type(Class<?> cls) {
        Map<String, Class<?>> pk_t = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getDeclaredAnnotation(Column.class);
            if (isPrimaryKey(column) > 0) {
                pk_t.put(ColumnNameHelper.getColumnName(field), field.getType());
            }
        }
        return pk_t;
    }

    /*
     * public static Map<String,Object> getPrimaryKeys_Values(Class<?> cls){
     * Map<String,Object> pk_v =new HashMap<>(); Field[]
     * fields=cls.getDeclaredFields(); for (Field field :fields){ Column
     * column=field.getDeclaredAnnotation(Column.class); if(isPrimaryKey(column)>0){
     * // pk_t.put(ColumnNameHelper.getColumnName(field),field.getType()); } }
     * return pk_v; }
     */
    public static List<String> getForeignKeyName(Class<?> cls) {
        List<String> fkNames = new ArrayList<String>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getDeclaredAnnotation(Column.class);
            Class<?> clazz = column.join();
            if (clazz != Object.class) {
                fkNames.add(ColumnNameHelper.getColumnName(field));
            }
        }
        return fkNames;
    }

    /**
     * 获取外键信息 返回map key: 外键名称 value: 外键关联表的类
     *
     * @param cls
     * @return map
     */
    public static Map<String, Class<?>> getForeignKeys(Class<?> cls) {
        Map<String, Class<?>> fks = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Column.class)) {
                continue;
            }
            Column column = field.getDeclaredAnnotation(Column.class);
            Class<?> clazz = column.join();
            if (clazz == Object.class) {
                continue;
            }
            fks.put(ColumnNameHelper.getColumnName(field), clazz);
        }
        return fks;
    }


    /**
     * 获取关联的列名和字段类型
     *
     * @param cls
     * @return
     */
    public static Map<String, Class<?>> getJoinColumnAndType(Class<?> cls) {
        Map<String, Class<?>> ct = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            ct.put(field.getName(), getJoinClass(field));
        }
        return ct;
    }

    /**
     * 获取o2m关联信息 （k->关联的表外键字段名 v->关联的类)
     *
     * @param cls
     * @return
     */
    public static Map<String, Class<?>> getJoinAndForeignKeys(Class<?> cls) {
        Map<String, Class<?>> tf = new HashMap<>();
        Field[] fields = cls.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(OneToMany.class)) {
                continue;
            }
            OneToMany o2m = field.getDeclaredAnnotation(OneToMany.class);
            Class<?> clazz = getJoinClass(field);
            String fkName = o2m.column();
            if (StringUtil.isEmpty(fkName)) {
                Map<String, Class<?>> foreignKeys = getForeignKeys(clazz);
                for (Map.Entry<String, Class<?>> ft : foreignKeys.entrySet()) {
                    if (cls == ft.getValue()) {
                        fkName = ft.getKey();
                    }
                }
            }
            tf.put(fkName, clazz);
        }
        return tf;
    }

    /**
     * 获取关联外键的表对应实体类
     *
     * @param field
     * @return
     */
    private static Class<?> getJoinClass(Field field) {
        OneToMany o2m = field.getDeclaredAnnotation(OneToMany.class);
        Class<?> clazz = o2m.join();
        if (clazz == Object.class) {
            if (Collection.class.isAssignableFrom(field.getType())) {
                ParameterizedType pm = (ParameterizedType) field.getGenericType();
                Class<?> fcls = (Class<?>) pm.getActualTypeArguments()[0];
                if (TableUtil.isTable(fcls)) {
                    clazz = fcls;
                }
            }
        }
        return clazz;
    }

    /**
     * 判断是否为主键 返回类型
     *
     * @param column
     * @return (0 : 不是主键) (1 : 普通主键) (2 : 自增主键) (3 : uuid生成主键)
     */
    public static int isPrimaryKey(Column column) {
        if (column == null) {
            throw new NotFoundFieldException("没有找到@Column注解...");
        }
        switch (column.id()) {
            case NO:
                return EnumId.NO.getCode();
            case YES:
                return EnumId.YES.getCode();
            case AUTO:
                return EnumId.AUTO.getCode();
            case UUID:
                return EnumId.UUID.getCode();
            default:
                return 0;
        }
    }
}
