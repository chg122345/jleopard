package com.leopardframework.bean;

import com.leopardframework.bean.property.PropsInfo;

import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/12
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
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
