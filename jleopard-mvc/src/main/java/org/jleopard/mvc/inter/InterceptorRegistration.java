/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-05  上午11:01
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.inter;


import org.jleopard.util.CollectionUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 拦截路径注解配置
 */

@Deprecated
public class InterceptorRegistration {

    private final List<String> includePatterns = new ArrayList();
    private final List<String> excludePatterns = new ArrayList();
    private int order = 0;

    public InterceptorRegistration addPathPatterns(String... patterns) {
        return this.addPathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration addPathPatterns(List<String> patterns) {
        this.includePatterns.addAll(patterns);
        return this;
    }

    public InterceptorRegistration excludePathPatterns(String... patterns) {
        return this.excludePathPatterns(Arrays.asList(patterns));
    }

    public InterceptorRegistration excludePathPatterns(List<String> patterns) {
        this.excludePatterns.addAll(patterns);
        return this;
    }

    public InterceptorRegistration order(int order) {
        this.order = order;
        return this;
    }

    protected int getOrder() {
        return this.order;
    }

    public boolean invoke(HttpServletRequest request) {
        String context = request.getContextPath();
        String url = request.getRequestURI();
        String uri = url.replace(context, "").replaceAll("/+", "/");
        if (CollectionUtil.isNotEmpty(excludePatterns)) {
            for (String pattern : excludePatterns) {
                if (Pattern.matches(pattern, uri)) {
                    return true;
                }
            }
        }
        if (CollectionUtil.isNotEmpty(includePatterns)) {
            for (String pattern : includePatterns) {
                if (Pattern.matches(pattern, uri)) {
                    return false;
                }
            }
        }
        return true;
    }
}
