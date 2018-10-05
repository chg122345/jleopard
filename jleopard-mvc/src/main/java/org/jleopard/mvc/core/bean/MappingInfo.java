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

import java.lang.reflect.Method;
import java.util.Set;


public class MappingInfo {

    private String url;

    private org.jleopard.mvc.core.ienum.Method imed;

    private Object newInstance;

    private Method method;

    private boolean renderJson;

    private Set<Class<? extends Interceptor>> interceptors;

    public MappingInfo(String url, org.jleopard.mvc.core.ienum.Method imed, Object newInstance, Method method, boolean renderJson, Set<Class<? extends Interceptor>> interceptors) {
        this.url = url;
        this.imed = imed;
        this.newInstance = newInstance;
        this.method = method;
        this.renderJson = renderJson;
        this.interceptors = interceptors;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public org.jleopard.mvc.core.ienum.Method getImed() {
        return imed;
    }

    public void setImed(org.jleopard.mvc.core.ienum.Method imed) {
        this.imed = imed;
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
