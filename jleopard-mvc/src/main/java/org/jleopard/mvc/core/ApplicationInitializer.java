/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:38
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core;

import org.jleopard.mvc.view.ViewResolver;
import org.jleopard.mvc.view.jsp.JSPViewResolver;

/**
 * 初始化应用配置
 */
public interface ApplicationInitializer {

    default String getBasePackage(){
        return "";
    }

    default ViewResolver getViewResolver(){
        return new JSPViewResolver();
    }
}
