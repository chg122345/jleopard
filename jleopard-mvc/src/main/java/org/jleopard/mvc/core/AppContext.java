/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:38
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core;

/**
 * 扫描的包
 */
public interface AppContext {

    default String getBasePackage(){
        return "";
    }
}
