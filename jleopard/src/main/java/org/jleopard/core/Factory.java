package org.jleopard.core;

import org.jleopard.generator.GeneratorFactory;
import org.jleopard.session.Configuration;
import org.jleopard.session.sessionFactory.SessionFactory;
import org.jleopard.util.ClassUtil;
import org.jleopard.xml.XmlFactoryBuilder;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime Jul 24, 2018 11:11:19 AM
 * 
 * Find a way for success and not make excuses for failure.
 *
 */
public final class Factory {
	
    public static SessionFactory getSessionFactory(String xmlPath) {
      return SessionFactory.getSessionFactory(getConfiguration(xmlPath));
    }

    public static GeneratorFactory getGeneratorFactory(String xmlPath) {
    	
        return GeneratorFactory.getGeneratorFactory(getConfiguration(xmlPath));
    }
    
    public static SessionFactory getSessionFactory(Configuration configuration) {
        return SessionFactory.getSessionFactory(configuration);
      }

      public static GeneratorFactory getGeneratorFactory(Configuration configuration) {
      	
          return GeneratorFactory.getGeneratorFactory(configuration);
      }
      
    private static Configuration getConfiguration(String xmlPath) {
    	 if (xmlPath.startsWith("classpath:")) {
             xmlPath = xmlPath.replace("classpath:", "").trim();
         }
         xmlPath = ClassUtil.getClassLoader().getResource(xmlPath).getPath();
         XmlFactoryBuilder builder = new XmlFactoryBuilder(xmlPath);
         XmlFactoryBuilder.XmlFactory factory = builder.getFactory();
         return factory.getConfiguration();
    }
}
