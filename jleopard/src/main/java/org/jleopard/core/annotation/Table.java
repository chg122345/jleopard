package org.jleopard.core.annotation;

import java.lang.annotation.*;
/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * Find a way for success and not make excuses for failure.
 *
 *
 *   Table 标记为对应数据库表
 *   value  相对应的数据库表名 默认 " "空串
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {
    String value() default "";
}
