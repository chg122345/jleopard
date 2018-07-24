package org.jleopard.exception;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/8
 * <p>
 * Find a way for success and not make excuses for failure.
 */
@SuppressWarnings("serial")
public class SessionException extends RuntimeException{

	public SessionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public SessionException() {
        super();
    }
    public SessionException(String message) {
        super(message);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SessionException(Throwable cause) {
        super(cause);
    }

}
