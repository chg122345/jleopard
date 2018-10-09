/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-03  下午8:00
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.servlet;

import org.jleopard.mvc.context.BeanContextUtil;
import org.jleopard.mvc.core.ApplicationInitializer;
import org.jleopard.mvc.core.annotation.*;
import org.jleopard.mvc.core.bean.Action;
import org.jleopard.mvc.core.bean.MappingInfo;
import org.jleopard.mvc.inter.Before;
import org.jleopard.mvc.inter.Clear;
import org.jleopard.mvc.inter.Interceptor;
import org.jleopard.mvc.view.View;
import org.jleopard.mvc.view.ViewResolver;
import org.jleopard.session.Configuration;
import org.jleopard.util.ClassUtil;
import org.jleopard.util.CollectionUtil;
import org.jleopard.util.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 转发控制器
 */
public class DispatcherServlet extends HttpServlet {

    private ApplicationInitializer appInitializer;

    private final static String DEFAULT_SERVLET_NAME = "default";

    private final static String SQL_SESSION_FACTORY_BEAN_NAME = "sqlSessionFactory";

    private Map<Action, MappingInfo> map = new HashMap<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() throws ServletException {
        super.init();
        this.initAppInitializer();
        this.initHandlerMapping();
        this.initBeanIoc();
        this.initInject();
    }


    @Override
    public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       /* String url = req.getRequestURI();
        if (url.contains("."))){
            RequestDispatcher rd = getServletContext().getNamedDispatcher(DEFAULT_SERVLET_NAME);
            if (rd == null) {
                throw new IllegalStateException("A RequestDispatcher could not be located for the default servlet '" + DEFAULT_SERVLET_NAME + "'");
            } else {
                rd.forward(req, resp);
            }
        }*/
        ViewResolver resolver = appInitializer.viewResolver();
        View view = resolver.resolveView();
        view.render(map, req, resp);
    }

    @Override
    public void destroy() {
        super.destroy();
        this.map.clear();
        BeanContextUtil.clearBean();
    }


    /**
     * 初始化配置
     */
    private void initAppInitializer() {
        Set<Class<?>> set = ClassUtil.getClassSetByPackagename("").stream().filter(i -> (i.isAnnotationPresent(Component.class) && Arrays.asList(i.getInterfaces()).contains(ApplicationInitializer.class))).collect(Collectors.toSet());
        // 基础配置
        for (Class<?> app : set) {
            try {
                this.appInitializer = (ApplicationInitializer) app.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        BeanContextUtil.registerBean(SQL_SESSION_FACTORY_BEAN_NAME, appInitializer.sqlSessionFactory(new Configuration()));
    }

    /**
     * 初始化映射uri 和 method
     */
    private void initHandlerMapping() {
        Set<Class<?>> set = ClassUtil.getClassSetByPackagename(appInitializer.basePackage()).stream().filter(i -> i.isAnnotationPresent(Controller.class)).collect(Collectors.toSet());
        set.stream().forEach(i -> {
            String var1 = "";
            Object newInstance = null;
            try {
                newInstance = i.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            Set<Class<? extends Interceptor>> inters = new HashSet<>();
            Before before = i.getDeclaredAnnotation(Before.class);
            if (before != null) {
                Class<? extends Interceptor>[] value = before.value();
                inters = Arrays.stream(value).collect(Collectors.toSet());
            }
            String key;
            Controller controller = i.getDeclaredAnnotation(Controller.class);
            key = controller.value();
            if (StringUtil.isEmpty(key)) {
                key = StringUtil.firstToLower(i.getSimpleName());
            }
            BeanContextUtil.registerBean(key, newInstance);
            Method[] methods = i.getDeclaredMethods();
            if (i.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = i.getDeclaredAnnotation(RequestMapping.class);
                String[] var3 = requestMapping.value();
                for (String var$ : var3) {
                    if (StringUtil.isEmpty(var$)) {
                        continue;
                    }
                    if (!var$.startsWith("/")) {
                        var1 = "/" + var$;
                    } else {
                        var1 = var$;
                    }
                    addMapping(var1, newInstance, methods, inters);
                }
            } else {
                addMapping(var1, newInstance, methods, inters);
            }
        });
    }

    /**
     * 获取 mapping
     *
     * @param var1
     * @param newInstance
     * @param methods
     */
    private void addMapping(String var1, Object newInstance, Method[] methods, final Set<Class<? extends Interceptor>> inters) {
        String var2;
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                Set<Class<? extends Interceptor>> methodInters = new HashSet<>();
                if (CollectionUtil.isNotEmpty(inters)) {
                    methodInters.addAll(inters);
                }
                Before before = method.getDeclaredAnnotation(Before.class);
                Clear clear = method.getDeclaredAnnotation(Clear.class);
                if (before != null) {
                    Class<? extends Interceptor>[] value = before.value();
                    methodInters.addAll(Arrays.stream(value).collect(Collectors.toSet()));
                }
                if (clear != null && CollectionUtil.isNotEmpty(methodInters)) {
                    Class<? extends Interceptor>[] value = clear.value();
                    methodInters.removeAll(Arrays.stream(value).collect(Collectors.toSet()));
                }
                RequestMapping requestMapping$ = method.getDeclaredAnnotation(RequestMapping.class);
                String[] var4 = requestMapping$.value();
                boolean renderJson = false;
                if (method.isAnnotationPresent(RenderJson.class)) {
                    renderJson = true;
                }
                for (String var$1 : var4) {
                    if (StringUtil.isNotEmpty(var$1) && !var$1.startsWith("/")) {
                        var2 = "/" + var$1;
                    } else {
                        var2 = var$1;
                    }
                    String url = var1 + var2;
                    Action action = new Action(url, requestMapping$.method());
                    MappingInfo mappingInfo = new MappingInfo(action, newInstance, method, renderJson, methodInters);
                    map.put(action, mappingInfo);
                }
            }
        }
    }

    /**
     * 初始化ioc容器
     */
    private void initBeanIoc() {
        Set<Class<?>> set = ClassUtil.getClassSetByPackagename(appInitializer.basePackage()).stream().filter(i -> (i.isAnnotationPresent(Component.class) || i.isAnnotationPresent(Service.class))).collect(Collectors.toSet());
        set.stream().forEach(i -> {
            Component component = i.getDeclaredAnnotation(Component.class);
            Service service = i.getDeclaredAnnotation(Service.class);
            String value = null;
            if (component != null) {
                value = component.value();
            }
            if (service != null) {
                value = service.value();
            }
            if (StringUtil.isEmpty(value)) {
                value = StringUtil.firstToLower(i.getSimpleName());
            }
            try {
                BeanContextUtil.registerBean(value, i.newInstance());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 依赖注入
     */
    private void initInject() {
        Map<String, Object> ioc = BeanContextUtil.getIoc();
        for (Map.Entry<String, Object> i : ioc.entrySet()) {
            Object target = i.getValue();
            Class<?> clazz = target.getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    Inject inject = field.getAnnotation(Inject.class);
                    String value = inject.value().trim();
                    if (StringUtil.isEmpty(value)) {
                        value = StringUtil.firstToLower(field.getType().getSimpleName());
                    }
                    field.setAccessible(true);
                    try {
                        field.set(target, ioc.get(value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
