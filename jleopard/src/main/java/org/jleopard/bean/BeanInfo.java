package org.jleopard.bean;


import java.util.List;

import org.jleopard.bean.property.PropsInfo;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/12
 * <p>
 * Find a way for success and not make excuses for failure.
 * <p>
 *  存储bean 信息..
 */
public class BeanInfo {

    private String id;  //xml bean 的id属性

    private String className; //xml bean 的 className 属性

    private List<PropsInfo> props;  //property信息

    public BeanInfo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<PropsInfo> getProps() {
        return props;
    }

    public void setProps(List<PropsInfo> props) {
        this.props = props;
    }
}
