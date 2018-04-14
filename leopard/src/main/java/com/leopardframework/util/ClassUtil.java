package com.leopardframework.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/7
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 *
 *
 *  类加载工具
 */
public class ClassUtil {

    /**
     * 获取类加载器
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取类路径
     */
    public static String getClassPath() {
        String classpath = "";
        URL resource = getClassLoader().getResource("");
        if (resource != null) {
            classpath = resource.getPath();
        }
        return classpath;
    }

    /**
     * 加载类（将自动初始化）
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 加载类
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }
    
    public static Set<Class<?>> getClassSetByPackagename(String packagename){
		Set<Class<?>> scls=new HashSet<>();
		if(StringUtil.isEmpty(packagename)){
			String packagePath=System.getProperty("user.dir")+"\\src";
			addClass(scls, packagePath, packagename);
			//throw new RuntimeException(" 实体类的位置没有找到...");
		}else {
			URL url = getClassLoader().getResource(packagename.replace(".", "/"));

			if (url != null) {
				String protocol = url.getProtocol();
				//	System.out.println("protocol : "+protocol);
				if (protocol.equals("file")) {
					String packagePath = url.getPath().replaceAll("%20", "");
					//	System.out.println("packagePath : "+packagePath);
					addClass(scls, packagePath, packagename);
				} else if (protocol.equals("jar")) {
					try {
						JarURLConnection jco = (JarURLConnection) url.openConnection();
						if (jco != null) {
							JarFile jf = jco.getJarFile();
							if (jf != null) {
								Enumeration<JarEntry> jes = jf.entries();
								while (jes.hasMoreElements()) {
									JarEntry je = jes.nextElement();
									String jen = je.getName();
									if (jen.endsWith(".class")) {
										String classname = jen.substring(0, jen.lastIndexOf(".")).replaceAll("/", ".");
										doAddClass(scls, classname);
									}
								}
							}
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		return scls;
	}

	public static void doAddClass(Set<Class<?>> scls, String classname) {
		Class<?> cls=loadClass(classname, false);
		scls.add(cls);
		
	}

	public static void addClass(Set<Class<?>> scls, String packagePath, String packagename) {
	File[] files=new File(packagePath).listFiles(new FileFilter() {
		
		@Override
		public boolean accept(File file) {
			
			return (file.isFile()&&file.getName().endsWith(".class"))||file.isDirectory();
		}
	});
		for(File file:files) {
			String fname=file.getName();
			if(file.isFile()) {
				String classname=fname.substring(0, fname.lastIndexOf("."));
				if(StringUtil.isNotEmpty(packagename)) {
					classname=packagename+"."+classname;
				} 
				doAddClass(scls,classname);
			}else {
				String subpackagepath=fname;
				if(StringUtil.isNotEmpty(subpackagepath)) {
					subpackagepath=packagePath+"/"+subpackagepath;
				}
				String subpackagename=fname;
				if(StringUtil.isNotEmpty(subpackagename)) {
					subpackagename=packagename+"."+subpackagename;
				}
				addClass(scls,subpackagepath,subpackagename);
			}
		}
	}


    /**
     * 是否为 int 类型（包含 Integer 类型）
     */
    public static boolean isInt(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class);
    }

    /**
     * 是否为 long 类型（包含 Long 类型）
     */
    public static boolean isLong(Class<?> type) {
        return type.equals(long.class) || type.equals(Long.class);
    }

    /**
     * 是否为 double 类型（包含 Double 类型）
     */
    public static boolean isDouble(Class<?> type) {
        return type.equals(double.class) || type.equals(Double.class);
    }

    /**
     * 是否为 String 类型
     */
    public static boolean isString(Class<?> type) {
        return type.equals(String.class);
    }
}
