package org.jleopard.exception;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/15
 * <p>
 * Find a way for success and not make excuses for failure.
 *  操作数据时可能产生的异常
 */
@SuppressWarnings("serial")
public class SqlSessionException extends Exception {


	public SqlSessionException() {
    }

    public SqlSessionException(String message) {
        super(message);
    }

    public SqlSessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlSessionException(Throwable cause) {
        super(cause);
    }

}
