/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-04  下午8:32
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.ienum;

public enum  ContentType {

    TEXT("text/plain;charset=UTF-8"),
    HTML("text/html;charset=UTF-8"),
    XML("text/xml;charset=UTF-8"),
    JSON("application/json;charset=UTF-8"),
    JAVASCRIPT("application/javascript;charset=UTF-8");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public String toString() {
        return value;
    }
}
