package com.leopardframework.core.session.sessionFactory;

import java.util.List;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/10
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 */
final class DevModelHelper {
    protected static void outListParameter(boolean DevModel,String sql,List values){
        if(DevModel){
            System.out.println(sql);
            System.out.print("Parameters : " );

            for (Object value:values){
                System.out.print(value+" ");
            }
            System.out.println();
        }
    }
    protected static void outArrayParameter(boolean DevModel,String sql,Object[] args){
        if(DevModel){
            System.out.println(sql);
            System.out.print("Parameters :" );
            for (Object arg:args){
                System.out.print(arg+" ");
            }
            System.out.println();
        }
    }

    protected static void outParameter(boolean DevModel, String sql, Object primaryKey) {
        if(DevModel){
            System.out.println(sql);
            System.out.print("Parameters :" +primaryKey);
            System.out.println();
        }
    }
}
