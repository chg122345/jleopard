package org.jleopard.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 23, 2018 2:45:09 PM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public class JavaTypeToDatabaseType {

	@SuppressWarnings("serial")
	private Map<String, Class<?>> strToType = new HashMap<String, Class<?>>() {

		{
            this.put("java.lang.String", String.class);
            this.put("java.lang.Integer", Integer.class);
            this.put("java.lang.Long", Long.class);
            this.put("java.sql.Date", java.sql.Date.class);
            this.put("java.lang.Double", Double.class);
            this.put("java.lang.Float", Float.class);
            this.put("java.lang.Boolean", Boolean.class);
            this.put("java.sql.Time", java.sql.Time.class);
            this.put("java.sql.Timestamp", java.sql.Timestamp.class);
            this.put("java.math.BigDecimal", java.math.BigDecimal.class);
            this.put("java.math.BigInteger", java.math.BigInteger.class);
            this.put("B", byte[].class);
        }
    };

    public JavaTypeToDatabaseType() {
    }

    public Class<?> getType(String typeString) {
        return (Class<?>)this.strToType.get(typeString);
    }
}
