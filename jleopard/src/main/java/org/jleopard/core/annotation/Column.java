package org.jleopard.core.annotation;


import java.lang.annotation.*;

import org.jleopard.core.EnumPrimary;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *
 *
 * 注解在类变量上的
 * value 数据库对应的字段名  默认 " "
 * isPrimary 是否为主键  默认 不是主键
 * unique 是否唯一约束   默认 false
 *
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";

    EnumPrimary isPrimary() default EnumPrimary.NO;

   boolean allowNull() default false;

   String relation() default "";  //外键关系 连接另一个实体类
}
