/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-08  下午8:58
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private static Map<String, Object> ioc = new ConcurrentHashMap<>(255);

    public static void registerBean(String beanName,Object instance){
        ioc.put(beanName,instance);
    }

    public static Object getBeanByName(String beanName){
        return ioc.get(beanName);
    }
}
