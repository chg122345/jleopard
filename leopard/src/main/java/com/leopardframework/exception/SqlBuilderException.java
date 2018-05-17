package com.leopardframework.exception;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
@SuppressWarnings("serial")
@Deprecated
public class SqlBuilderException extends RuntimeException{

    public SqlBuilderException() {
    }

    public SqlBuilderException(String msg) {
        super(msg);
    }
}
