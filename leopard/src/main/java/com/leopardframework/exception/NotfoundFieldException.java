package com.leopardframework.exception;

/**
 * 
 * 字段丢失/没有找到异常
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
public class NotfoundFieldException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	 public NotfoundFieldException() {
	        super();
	    }

	    /**
	     * 
	     * 	字段丢失/没有找到异常
	     * @param   s   the detail message.
	     */
	    public NotfoundFieldException(String s) {
	        super(s);
	    }

}
