package org.jleopard.mvc;


import org.jleopard.mvc.core.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;


/**
 * Unit test for simple App.
 */
public class AppTest {


    public String tt(@RequestParam("name") String name, HttpServletRequest req, @RequestParam("pass")String pass){
        String a ="aaa";
        return "";
    }

    /*public static void main(String[] args) {

        String pattern = "/user/.?";
        String content = "/user/2/55";
        System.out.println(Pattern.matches(pattern,content));

        String [] a = new String[]{"1"};
        String [] b = new String[0];
        String [] c = null;
        System.out.println(a.getClass() == b.getClass());
        System.out.println(ArrayUtil.isEmpty(b));
    }*/
 /*   public static void main(String[] args) throws NoSuchMethodException {
        AppTest a = new AppTest();
        Method method = a.getClass().getMethod("tt",String.class,String.class,HttpServletRequest.class);
        Annotation[][] annotations = method.getParameterAnnotations();
        for (int i = 0; i < annotations.length ; i++) {
            Annotation[] annotation = annotations[i];
            for (Annotation param:annotation){
                RequestParam pm = (RequestParam) param;
                System.out.println(param.annotationType()+ "-->" + pm.value());
            }

        }

    }*/

   /* public static void main(String[] args) throws NoSuchMethodException, NotFoundException {
        AppTest a = new AppTest();
        Method method = a.getClass().getMethod("tt",String.class,String.class,HttpServletRequest.class);
        String[] paramaterName = MethodUtil.getAllParamaterName(method);
        Arrays.stream(paramaterName).forEach(System.out::println);
    }*/

    public static void main(String[] args) throws NoSuchMethodException {
        AppTest a = new AppTest();
        Map<Integer,String> map = new HashMap();
        Method method = a.getClass().getMethod("tt",String.class,HttpServletRequest.class,String.class);
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i <parameters.length ; i++) {
            System.out.println(parameters[i]);
            RequestParam pm = parameters[i].getAnnotation(RequestParam.class);
            System.out.println(pm);
            if (pm != null){
                map.put(i,pm.value());
            }
        }

        map.forEach((k,v)->System.out.print("k-->" + k +"value-->" +v));
    }
}
