package com.leopardframework.test.entity;

import com.leopardframework.core.annotation.Column;
import com.leopardframework.core.annotation.Table;
import com.leopardframework.core.enums.Primary;

import java.util.Date;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/11
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
@Table
public class Student {
    @Column(isPrimary = Primary.AUTOINCREMENT)
    private Long id;
    @Column
    private String name;
    @Column
    private Date created;

    public Student() {
    }

    public Student(Long id, String name, Date created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", created=" + created +
                '}';
    }
}
