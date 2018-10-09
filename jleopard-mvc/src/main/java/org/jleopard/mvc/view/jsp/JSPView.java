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
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jleopard.mvc.core.bean.Action;
import org.jleopard.mvc.core.bean.MappingInfo;
import org.jleopard.mvc.core.ienum.ContentType;
import org.jleopard.mvc.core.ienum.ErrorPage;
import org.jleopard.mvc.inter.Interceptor;
import org.jleopard.mvc.inter.InterceptorRegistration;
import org.jleopard.mvc.upload.MultipartFile;
import org.jleopard.mvc.upload.UploadFile;
import org.jleopard.mvc.util.MethodUtil;
import org.jleopard.mvc.view.View;
import org.jleopard.mvc.view.ViewResolverException;
import org.jleopard.util.ArrayUtil;
import org.jleopard.util.ClassUtil;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * JSP模板渲染
 */
public class JSPView implements View {

    private String contentType = ContentType.HTML.value();

    private String prefix = "";

    private String suffix = ".jsp";

    public JSPView(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public JSPView() {
    }

    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public void render(Map<Action, ?> map, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String context = req.getContextPath();
        String url = req.getRequestURI();
        String uri = url.replace(context, "").replaceAll("/+", "/");
        String method = req.getMethod();
        org.jleopard.mvc.core.ienum.Method mappingMethod = getMethod(method);
        Action action = new Action(uri, mappingMethod);
        Object value = null;
        boolean renderJson = false;
        MappingInfo mappingInfo = (MappingInfo) map.get(action);
        if (mappingInfo == null) {
            action = new Action(uri, org.jleopard.mvc.core.ienum.Method.ALL);
            mappingInfo = (MappingInfo) map.get(action);
        }
        if (mappingInfo == null) {  // 没有找到映射方法 404
            resp.setStatus(404);
            renderErrorPage(404, resp);
            return;
        }
        Set<Class<? extends Interceptor>> interceptors = mappingInfo.getInterceptors();
        try {
            if (CollectionUtil.isNotEmpty(interceptors)) {
                for (Class<? extends Interceptor> inter : interceptors) {
                    InterceptorRegistration registration = new InterceptorRegistration();
                    Method m1 = inter.getDeclaredMethod("preHandle", HttpServletRequest.class, HttpServletResponse.class, InterceptorRegistration.class);
                    boolean invoke = (boolean) m1.invoke(inter.newInstance(), req, resp, registration);
                    if (!invoke) {
                        resp.setStatus(403);
                        renderErrorPage(403, resp);
                        break;
                    }
                }
            }
            Method method$ = mappingInfo.getMethod();
            value = method$.invoke(mappingInfo.getNewInstance(), initMethodParam(req, resp, method$));
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            renderErrorPage(500, resp);
        }
        renderJson = mappingInfo.isRenderJson();
        if (value != null) {
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
                        req.getRequestDispatcher(page).forward(req, resp);
                    }
                } else {
                    throw new ViewResolverException("请求方法没有 @RenderJson注解");
                }
            }
        }
    }

    private org.jleopard.mvc.core.ienum.Method getMethod(String requestMethod) {
        if (requestMethod.equalsIgnoreCase("get")) {
            return org.jleopard.mvc.core.ienum.Method.GET;
        } else if (requestMethod.equalsIgnoreCase("post")) {
            return org.jleopard.mvc.core.ienum.Method.POST;
        } else if (requestMethod.equalsIgnoreCase("put")) {
            return org.jleopard.mvc.core.ienum.Method.PUT;
        } else if (requestMethod.equalsIgnoreCase("delete")) {
            return org.jleopard.mvc.core.ienum.Method.DELETE;
        }
        return null;
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

    private Object[] initMethodParam(HttpServletRequest req, HttpServletResponse resp, Method var2) throws FileUploadException, UnsupportedEncodingException {
        Map<String, String[]> paraMap = req.getParameterMap();

        Class<?>[] paraTypes = var2.getParameterTypes();
        Map<Integer, String> paraNamesMap = MethodUtil.getParamaterNames(var2);
        Object[] paraValues = new Object[paraTypes.length];
        List<MultipartFile> list = new ArrayList<>();
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);
        if (isMultipart) {
            paraMap = new HashMap();
            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    String value = item.getString("UTF-8");
                    String[] curParam = paraMap.get(fieldName);
                    if (curParam == null) {
                        paraMap.put(fieldName, new String[]{value});
                    } else {
                        String[] newParam = StringUtil.addStringToArray(curParam, value);
                        paraMap.put(fieldName, newParam);
                    }
                } else {
                    MultipartFile uploadFile = new UploadFile(item);
                    list.add(uploadFile);
                }
            }
        }
        MultipartFile[] multipartFiles = new MultipartFile[list.size()];
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
            } else if (var$ == String.class && !paraMap.isEmpty()) {
                String paraName = paraNamesMap.get(i);
                if (StringUtil.isNotEmpty(paraName)) {
                    String value = getvalue(paraMap, paraName);
                    paraValues[i] = value;
                    continue;
                }
            } else if (var$ == Integer.class && !paraMap.isEmpty()) {
                String paraName = paraNamesMap.get(i);
                if (StringUtil.isNotEmpty(paraName)) {
                    String value = getvalue(paraMap, paraName);
                    paraValues[i] = Integer.valueOf(value);
                    continue;
                }
            } else if (var$ == Long.class && !paraMap.isEmpty()) {
                String paraName = paraNamesMap.get(i);
                if (StringUtil.isNotEmpty(paraName)) {
                    String value = getvalue(paraMap, paraName);
                    paraValues[i] = Long.valueOf(value);
                    continue;
                }
            } else if (var$ == Double.class && !paraMap.isEmpty()) {
                String paraName = paraNamesMap.get(i);
                if (StringUtil.isNotEmpty(paraName)) {
                    String value = getvalue(paraMap, paraName);
                    paraValues[i] = Double.valueOf(value);
                    continue;
                }
            } else if (var$ == Float.class && !paraMap.isEmpty()) {
                String paraName = paraNamesMap.get(i);
                if (StringUtil.isNotEmpty(paraName)) {
                    String value = getvalue(paraMap, paraName);
                    paraValues[i] = Float.valueOf(value);
                    continue;
                }
            } else if (var$ == Byte.class && !paraMap.isEmpty()) {
                String paraName = paraNamesMap.get(i);
                if (StringUtil.isNotEmpty(paraName)) {
                    String value = getvalue(paraMap, paraName);
                    paraValues[i] = Byte.valueOf(value);
                    continue;
                }
            } else if (var$ == MultipartFile.class) {
                // 文件
                paraValues[i] = CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
                continue;
            } else if (var$ == multipartFiles.getClass()) {
                // 多文件
                paraValues[i] = list.toArray(multipartFiles);
                continue;
            } else {  //封装类型
                Field[] fields = var$.getDeclaredFields();
                Object instance;
                try {
                    instance = var$.newInstance();
                    for (Field field : fields) {
                        String[] strings = paraMap.get(field.getName());
                        if (ArrayUtil.isNotEmpty(strings)) {
                            String value = Arrays.toString(strings).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
                            if (StringUtil.isNotEmpty(value)) {
                                field.setAccessible(true);
                                field.set(instance, ClassUtil.changeType(field, value));
                            }
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

    private String getvalue(Map<String, String[]> paraMap, String paraName) {
        String[] vales = paraMap.get(paraName);
        return Arrays.toString(vales).replaceAll("\\[|\\]", "").replaceAll("\\s", ",");
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
