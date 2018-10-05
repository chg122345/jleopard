/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:11
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.view.jsp;

import com.alibaba.fastjson.JSON;
import org.jleopard.mvc.core.annotation.Component;
import org.jleopard.mvc.core.bean.MappingInfo;
import org.jleopard.mvc.core.ienum.ContentType;
import org.jleopard.mvc.core.ienum.ErrorPage;
import org.jleopard.mvc.inter.Interceptor;
import org.jleopard.mvc.inter.InterceptorRegistration;
import org.jleopard.mvc.view.View;
import org.jleopard.mvc.view.ViewResolverException;
import org.jleopard.util.ClassUtil;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static org.jleopard.mvc.core.ienum.Method.ALL;

/**
 * JSP模板渲染
 */
@Component("viewResolver")
public class JSPViewResolver implements View {

    private String contentType = ContentType.HTML.value();

    private String prefix = "";

    private String suffix = ".jsp";

    public JSPViewResolver(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public JSPViewResolver() {
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String context = req.getContextPath();
        String url = req.getRequestURI();
        String uri = url.replace(context, "").replaceAll("/+", "/");
        String method = req.getMethod();
        Object value = null;
        boolean renderJson = false;
        try {
            for (Map.Entry<String, ?> $var : map.entrySet()) {
                if (uri.equals($var.getKey())) {
                    MappingInfo var1 = (MappingInfo) $var.getValue();
                    Set<Class<? extends Interceptor>> interceptors = var1.getInterceptors();
                    Method var2 = var1.getMethod();
                    if ((var1.getImed() == ALL) || method.equalsIgnoreCase(var1.getImed().getValue())) {
                        if (CollectionUtil.isNotEmpty(interceptors)) {
                            for (Class<? extends Interceptor> inter : interceptors) {
                                InterceptorRegistration registration = new InterceptorRegistration();
                                Method m1 = inter.getDeclaredMethod("preHandle", HttpServletRequest.class, HttpServletResponse.class, InterceptorRegistration.class);
                                boolean invoke = (boolean) m1.invoke(inter.newInstance(),req, resp, registration);
                                if (! invoke) {
                                    renderErrorPage(403, resp);
                                    break;
                                }
                            }
                        }
                        value = var2.invoke(var1.getNewInstance(), initMethodParam(req, resp, var2));
                        renderJson = var1.isRenderJson();
                        break;
                    } else {
                        renderErrorPage(405, resp);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            resp.getWriter().write("500 Exception:\r\n" + Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", "").replaceAll("\\s", "\r\n"));
            resp.getWriter().close();
        }

        if (value == null) {
            renderErrorPage(404, resp);
            return;
        }
        if (renderJson) {
            String var = JSON.toJSONString(value);
            resp.setContentType(ContentType.JSON.value());
            resp.getWriter().write(var);
            resp.getWriter().close();
        } else {
            if (value instanceof String) {
                if (((String) value).startsWith("redirect:")) {
                    String uri$ = ((String) value).replace("redirect:", "");
                    resp.sendRedirect(uri$);
                } else {
                    String page = this.prefix + value + this.suffix;
                    //resp.setContentType(this.getContentType());
                    req.getRequestDispatcher(page).forward(req, resp);
                }
            } else {
                throw new ViewResolverException("请求方法没有 @RenderJson注解");
            }
        }
    }


    private void renderErrorPage(int code, HttpServletResponse resp) throws IOException {
        resp.setContentType(this.getContentType());
        String page;
        switch (code) {
            case 401:
                page = ErrorPage.HTML_401.value();
                break;
            case 403:
                page = ErrorPage.HTML_403.value();
                break;
            case 404:
                page = ErrorPage.HTML_404.value();
                break;
            case 405:
                page = ErrorPage.HTML_405.value();
                break;
            case 500:
                page = ErrorPage.HTML_500.value();
                break;
            default:
                page = ErrorPage.HTML_ERR.value();
        }
        resp.getWriter().write(page);
        resp.getWriter().close();
    }

    private Object[] initMethodParam(HttpServletRequest req, HttpServletResponse resp, Method var2) {
        Class<?>[] paraTypes = var2.getParameterTypes();
        Map<String, String[]> paraMap = req.getParameterMap();
        Object[] paraValues = new Object[paraTypes.length];
        for (int i = 0; i < paraTypes.length; i++) {
            Class<?> var$ = paraTypes[i];
            if (var$ == HttpServletRequest.class) {
                paraValues[i] = req;
                continue;
            } else if (var$ == HttpServletResponse.class) {
                paraValues[i] = resp;
                continue;
            } else if (var$ == HttpSession.class) {
                paraValues[i] = req.getSession();
                continue;
            } else if (var$ == String.class) {
                for (Map.Entry<String, String[]> param : paraMap.entrySet()) {
                    String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
                    paraValues[i] = value;
                }
                continue;
            } else {  //封装类型
                Field[] fields = var$.getDeclaredFields();
                Object instance = null;
                try {
                    instance = var$.newInstance();
                    for (Field field : fields) {
                        String value = req.getParameter(field.getName());
                        if (StringUtil.isNotEmpty(value)) {
                            field.setAccessible(true);
                            field.set(instance, ClassUtil.changeType(field, value));
                        }
                    }
                } catch (Exception e) {
                    throw new ViewResolverException(e);
                }
                paraValues[i] = instance;
            }
        }
        return paraValues;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }
}
