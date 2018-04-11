package com.leopardframework.core;

import com.leopardframework.plugins.DBPlugin;
import com.leopardframework.util.PropsUtil;

import java.sql.Connection;
import java.util.Properties;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class Config {
    public static Connection getConnection(){
        Properties pros=PropsUtil.loadProps("config.properties");
        String driver=pros.getProperty(Constant.JDBCDRIVER);
        String url=pros.getProperty(Constant.JDBCURL);
        String username=pros.getProperty(Constant.JDBCUSER);
        String password=pros.getProperty(Constant.JDBCPASSWORD);
        DBPlugin db=new DBPlugin(driver,url,username,password);
        return db.getConn();
    }
    public static boolean getDevModel(){
        return Constant.DEV;
    }
}
