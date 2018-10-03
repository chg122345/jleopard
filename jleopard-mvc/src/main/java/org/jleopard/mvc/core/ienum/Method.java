/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-09-26  上午11:39
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.ienum;

/**
 * 请求方法
 */
public enum Method {
    ALL(0,"all"),GET(1,"get"),POST(2,"post"),PUT(3,"put"),DELETE(4,"delete");

    private int code;

    private String value;


    Method(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
