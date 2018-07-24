package org.jleopard.logging.log;

import java.lang.reflect.Constructor;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * Find a way for success and not make excuses for failure.
 */
public final class LogFactory {

    public static final String MARKER = "LEOPARD";
    private static Constructor<? extends Log> logConstructor;

    static {
        tryImplementation(new Runnable() {
            @Override
            public void run() {
                useSlf4jLogging();
            }
        });

        tryImplementation(new Runnable() {
            @Override
            public void run() {
                useJdkLogging();
            }
        });
        tryImplementation(new Runnable() {
            @Override
            public void run() {
                useLog4JLogging();
            }
        });
    }

    private LogFactory() {
    }

    public static Log getLog(Class<?> aClass) {
        return getLog(aClass.getName());
    }

    public static Log getLog(String logger) {
        try {
            return logConstructor.newInstance(logger);
        } catch (Throwable t) {
            throw new LogException("Error creating logger for logger " + logger + ".  Cause: " + t, t);
        }
    }

    public static synchronized void useCustomLogging(Class<? extends Log> clazz) {
        setImplementation(clazz);
    }

    public static synchronized void useSlf4jLogging() {
        setImplementation(org.jleopard.logging.slf4j.Slf4jImpl.class);
    }

    public static synchronized void useJdkLogging() {
        setImplementation(org.jleopard.logging.jdk.JdkLoggingImpl.class);
    }
    public static synchronized void useLog4JLogging() {
        setImplementation(org.jleopard.logging.log4j.Log4jImpl.class);
    }
    private static void tryImplementation(Runnable runnable) {
        if (logConstructor == null) {
            try {
                runnable.run();
            } catch (Throwable t) {
            }
        }
    }

    private static void setImplementation(Class<? extends Log> implClass) {
        try {
            Constructor<? extends Log> candidate = implClass.getConstructor(String.class);
            Log log = candidate.newInstance(LogFactory.class.getName());
            if (log.isDebugEnabled()) {
                log.debug("Logging initialized using '" + implClass + "' adapter.");
            }
            logConstructor = candidate;
        } catch (Throwable t) {
            throw new LogException("Error setting Log implementation.  Cause: " + t, t);
        }
    }
}
