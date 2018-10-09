/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-09  上午10:22
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.bean;

import org.jleopard.mvc.core.ienum.Method;

import java.io.Serializable;
import java.util.Objects;

public class Action implements Serializable {

    private String uri;

    private Method method;

    public Action() {
    }

    public Action(String uri, Method method) {
        this.uri = uri;
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action action = (Action) o;
        return Objects.equals(uri, action.uri) &&
                method == action.method;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, method);
    }
}
