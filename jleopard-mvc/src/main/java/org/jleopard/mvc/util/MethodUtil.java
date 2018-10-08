/**
 * @author (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @date 2018-10-08  下午4:07
 *
 * <p>
 * Find a way for success and not make excuses for failure.
 * </p>
 */

package org.jleopard.mvc.util;

import org.jleopard.mvc.core.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class MethodUtil {


   /* public static String[] getAllParamaterName(Method method)
            throws NotFoundException {
        Class<?> clazz = method.getDeclaringClass();
        ClassPool pool = ClassPool.getDefault();
        pool.appendClassPath(new LoaderClassPath(ClassUtil.getClassLoader()));
        CtClass clz = pool.get(clazz.getName());
        CtClass[] params = new CtClass[method.getParameterTypes().length];
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            params[i] = pool.getCtClass(method.getParameterTypes()[i].getName());
        }
        CtMethod cm = clz.getDeclaredMethod(method.getName(), params);
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        String[] paramNames = new String[cm.getParameterTypes().length];
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }*/

    /**
     *  获取方法参数名
     * @param method
     * @return map(k-->参数位置,v-->参数名)
     */
    public static  Map<Integer,String> getParamaterNames(Method method){
        Map<Integer,String> map = new HashMap();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i <parameters.length ; i++) {
            RequestParam pm = parameters[i].getAnnotation(RequestParam.class);
            if (pm != null){
                map.put(i,pm.value());
            }
        }
        return map;
    }
}
