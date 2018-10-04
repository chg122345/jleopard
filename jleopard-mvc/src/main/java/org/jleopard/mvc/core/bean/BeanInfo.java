/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-09-28  下午5:26
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.bean;

public class BeanInfo {

    private String name;

    private Object newInstance;

    public BeanInfo(String name, Object newInstance) {
        this.name = name;
        this.newInstance = newInstance;
    }

    public BeanInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getNewInstance() {
        return newInstance;
    }

    public void setNewInstance(Object newInstance) {
        this.newInstance = newInstance;
    }
}
