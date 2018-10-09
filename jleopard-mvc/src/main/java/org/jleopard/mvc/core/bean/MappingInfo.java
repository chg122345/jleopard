/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-09-26  下午2:39
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.bean;

import org.jleopard.mvc.inter.Interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


public class MappingInfo {

    private Action action; // uri method

    private Object newInstance;

    private Method method;

    private boolean renderJson;

    private Set<Class<? extends Interceptor>> interceptors;

    public MappingInfo(Action action, Object newInstance, Method method, boolean renderJson, Set<Class<? extends Interceptor>> interceptors) {
        this.action = action;
        this.newInstance = newInstance;
        this.method = method;
        this.renderJson = renderJson;
        this.interceptors = interceptors;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Object getNewInstance() {
        return newInstance;
    }

    public void setNewInstance(Object newInstance) {
        this.newInstance = newInstance;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public boolean isRenderJson() {
        return renderJson;
    }

    public void setRenderJson(boolean renderJson) {
        this.renderJson = renderJson;
    }

    public Set<Class<? extends Interceptor>> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(Set<Class<? extends Interceptor>> interceptors) {
        this.interceptors = interceptors;
    }
}
