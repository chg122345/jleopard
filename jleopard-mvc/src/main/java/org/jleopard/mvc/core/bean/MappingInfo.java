/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-09-26  下午2:39
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Method;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MappingInfo {

    private String url;

    private org.jleopard.mvc.core.ienum.Method imed;

    private Object newInstance;

    private Method method;

    private boolean renderJson;

}
