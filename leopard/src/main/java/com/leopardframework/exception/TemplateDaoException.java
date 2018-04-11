package com.leopardframework.exception;

/**
 * 
 * 模板运行时异常
 *
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class TemplateDaoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public TemplateDaoException() {
		super();
	}
	
	/**
	 * 
	 * @param message 错误信息
	 */
	public TemplateDaoException(String message) {
        super(message);
    }

}
