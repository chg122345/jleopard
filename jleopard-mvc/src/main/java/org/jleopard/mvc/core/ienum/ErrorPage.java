/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-04  下午8:34
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.core.ienum;


public enum ErrorPage {

    HTML_401(401,"<html><head><title>401 Unauthorized</title></head><body bgcolor='white'><center><h1>401 Unauthorized</h1></center><hr> 没有授权..</body></html>"),
    HTML_403(403,"<html><head><title>403 Forbidden</title></head><body bgcolor='white'><center><h1>403 Forbidden</h1></center><hr>不允许访问..</body></html>"),
    HTML_404(404,"<html><head><title>404 Not Found</title></head><body bgcolor='white'><center><h1>404 Not Found</h1></center><hr> 页面找不到了...</body></html>"),
    HTML_405(405,"<html><head><title>405 Not Allowed</title></head><body bgcolor='white'><center><h1>405 Not Allowed</h1></center><hr> 方法不被允许...</body></html>"),
    HTML_500(500,"<html><head><title>500 Internal Server Error</title></head><body bgcolor='white'><center><h1>500 Internal Server Error</h1></center><hr>服务器出错了..</body></html>"),
    HTML_ERR(0,"<html><head><title>Unknown Error</title></head><body bgcolor='white'><center><h1>Unknown Error</h1></center><hr>未知错误..</body></html>");

    private final int code;

    private final String value;

    ErrorPage(int code,String value) {
        this.code = code;
        this.value = value;
    }
    public String value() {
        return value;
    }

}
