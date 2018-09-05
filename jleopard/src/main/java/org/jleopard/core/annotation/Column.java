package org.jleopard.core.annotation;


import java.lang.annotation.*;

import org.jleopard.core.EnumId;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 * <p>
 * 注解在类变量上的
 * <p>
 * value 数据库对应的字段名  默认 " "
 * <p>
 * id 是否为主键  默认 不是主键
 * <p>
 * unique 是否唯一约束   默认 false
 * <p>
 * allowNull 建表时是否允许字段为空值 默认 false
 * <p>
 * join 外键关系 连接另一个实体类
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";

    EnumId id() default EnumId.NO;

    boolean unique() default false;
    
    boolean allowNull() default false;

   Class<?> join() default Object.class;  //外键关系 连接另一个实体类
}
