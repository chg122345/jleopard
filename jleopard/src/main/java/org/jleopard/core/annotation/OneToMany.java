package org.jleopard.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Sep 5, 2018 10:09:11 AM
 * 
 * Find a way for success and not make excuses for failure.
 * <p>
 * join 对应的表的实体类
 * <p>
 * column 关联另一张表的外键字段名
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OneToMany {
	
	Class<?> join() default Object.class;
	
	String column() default "";

}
