package com.leopardframework.core;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
public class JavaTypeToDatabaseType {

    @SuppressWarnings("serial")
	private Map<String, Class<?>> strToType = new HashMap<String, Class<?>>() {

		{
            this.put("java.lang.String", String.class);
            this.put("java.lang.Integer", Integer.class);
            this.put("java.lang.Long", Long.class);
            this.put("java.sql.Date", Date.class);
            this.put("java.lang.Double", Double.class);
            this.put("java.lang.Float", Float.class);
            this.put("java.lang.Boolean", Boolean.class);
            this.put("java.sql.Time", Time.class);
            this.put("java.sql.Timestamp", Timestamp.class);
            this.put("java.math.BigDecimal", BigDecimal.class);
            this.put("java.math.BigInteger", BigInteger.class);
            this.put("B", byte[].class);
        }
    };

    public JavaTypeToDatabaseType() {
    }

    public Class<?> getType(String typeString) {
        return (Class<?>)this.strToType.get(typeString);
    }
}
