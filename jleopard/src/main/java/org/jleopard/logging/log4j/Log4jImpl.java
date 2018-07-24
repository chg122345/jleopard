package org.jleopard.logging.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jleopard.logging.log.Log;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/9
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public class Log4jImpl implements Log {
    private static final String FQCN = Log4jImpl.class.getName();

    private final Logger log;

    public Log4jImpl(String clazz) {
        log = Logger.getLogger(clazz);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void error(String s, Throwable e) {
        log.log(FQCN, Level.ERROR, s, e);
    }

    @Override
    public void error(String s) {
        log.log(FQCN, Level.ERROR, s, null);
    }

    @Override
    public void debug(String s) {
        log.log(FQCN, Level.DEBUG, s, null);
    }

    @Override
    public void trace(String s) {
        log.log(FQCN, Level.TRACE, s, null);
    }

    @Override
    public void warn(String s) {
        log.log(FQCN, Level.WARN, s, null);
    }

    @Override
    public void info(String s) {
            log.log(FQCN, Level.INFO, s, null);
    }
}
