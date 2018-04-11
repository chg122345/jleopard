package com.leopardframework.logging.jdk;

import com.leopardframework.logging.log.Log;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class JdkLoggingImpl implements Log {
    private final Logger log;

    public JdkLoggingImpl(String clazz) {
        log = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isLoggable(Level.FINE);
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isLoggable(Level.FINER);
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(Level.SEVERE, s, e);
    }

    @Override
    public void error(String s) {
        log.log(Level.SEVERE, s);
    }

    @Override
    public void debug(String s) {
        log.log(Level.FINE, s);
    }

    @Override
    public void trace(String s) {
        log.log(Level.FINER, s);
    }

    @Override
    public void warn(String s) {
        log.log(Level.WARNING, s);
    }

    @Override
    public void info(String s) {
        log.log(Level.INFO,s);
    }
}
