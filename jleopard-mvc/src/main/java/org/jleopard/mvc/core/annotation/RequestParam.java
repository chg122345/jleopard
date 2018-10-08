/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-08  下午2:24
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestParam {
    String value(); //参数名
}
