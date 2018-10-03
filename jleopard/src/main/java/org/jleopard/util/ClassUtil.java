package org.jleopard.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
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
			String packagePath = getClassPath();
			addClass(scls, packagePath, packagename);
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

	private static void doAddClass(Set<Class<?>> scls, String classname) {
		Class<?> cls=loadClass(classname, false);
		scls.add(cls);
	}

	private static void addClass(Set<Class<?>> scls, String packagePath, String packagename) {
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
	 * 类型转换
	 * @param field
	 * @param arg
	 * @return
	 */
	public static Object changeType(Field field, Object arg) {
		String str = String.valueOf(arg);
		if (StringUtil.isEmpty(str)) {
			return null;
		}
		Class<?> type = field.getType();
		if (type == Long.class || type == long.class) {
			return Long.valueOf(str);
		}
		if (type == Integer.class || type == int.class) {
			return Integer.valueOf(str);
		}
		if (type == Byte.class || type == byte.class) {
			return Byte.valueOf(str);
		}
		if (type == Double.class || type == double.class) {
			return Double.valueOf(str);
		}
		if (type == Float.class || type == float.class) {
			return Float.valueOf(str);
		}
		if (type == java.util.Date.class) {
			return DateUtil.parseDatetime(str);
		}
		if (type == java.sql.Date.class) {
			return java.sql.Date.valueOf(str);
		}
		if (type == java.sql.Timestamp.class) {
			return java.sql.Timestamp.valueOf(str);
		}
		return str;
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
