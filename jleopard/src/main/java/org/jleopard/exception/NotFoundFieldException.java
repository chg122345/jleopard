package org.jleopard.exception;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/16
 * <p>
 * Find a way for success and not make excuses for failure.
 */
@SuppressWarnings("serial")
public class NotFoundFieldException extends IllegalArgumentException {

	public NotFoundFieldException() {
    }

    public NotFoundFieldException(String s) {
        super(s);
    }

    public NotFoundFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
