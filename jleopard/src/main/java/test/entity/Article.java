package test.entity;

import org.jleopard.core.EnumPrimary;
import org.jleopard.core.annotation.Column;
import org.jleopard.core.annotation.Table;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/18
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
@Table("article")
public class Article {

    @Column(isPrimary = EnumPrimary.AUTOINCREMENT)
    private int id;
    @Column(allowNull = true)
    private String name;
    @Column(value = "user_id",relation = "user_id")
    private User user;

    public Article() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", user=" + user +
                '}';
    }
}
