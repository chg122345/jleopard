package com.leopardframework.loadxml;

import java.io.Serializable;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/12
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *  全局信息 加载到上下文中
 */
@Deprecated
public  class XmlContext implements Serializable {

    public String entityPackage;  //实体对象类所在包

    public String generatorPackage;  //逆向工程生成JavaBean 包

    public XmlContext() {
    }

    public String getEntityPackage() {
        return entityPackage;
    }

    public void setEntityPackage(String entityPackage) {
        this.entityPackage = entityPackage;
    }

    public String getGeneratorPackage() {
        return generatorPackage;
    }

    public void setGeneratorPackage(String generatorPackage) {
        this.generatorPackage = generatorPackage;
    }
}
