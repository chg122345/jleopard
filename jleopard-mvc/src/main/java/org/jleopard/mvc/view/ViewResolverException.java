/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:46
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.view;

public class ViewResolverException extends RuntimeException {

    public ViewResolverException(String message) {
        super(message);
    }

    public ViewResolverException(String message, Throwable cause) {
        super(message, cause);
    }

    public ViewResolverException(Throwable cause) {
        super(cause);
    }
}
