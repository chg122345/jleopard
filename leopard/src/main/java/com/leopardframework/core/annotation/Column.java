package com.leopardframework.core.annotation;

import com.leopardframework.core.enums.Primary;

import java.lang.annotation.*;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
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

    Primary isPrimary() default Primary.NO;

   boolean allowNull() default false;

   boolean isForeginKey() default false;
}
