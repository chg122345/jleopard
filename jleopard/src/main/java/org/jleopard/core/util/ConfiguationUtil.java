package org.jleopard.core.util;

import org.jleopard.session.Configuration;
import org.jleopard.util.ClassUtil;
import org.jleopard.xml.XmlFactoryBuilder;

/**
 * @Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * @DateTime 2018-09-08  下午10:44
 *
 * <p>
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * </p>
 */
public class ConfiguationUtil {

    public static Configuration getConfiguration(String xmlPath) {
        if (xmlPath.startsWith("classpath:")) {
            xmlPath = xmlPath.replace("classpath:", "").trim();
        }
        xmlPath = ClassUtil.getClassLoader().getResource(xmlPath).getPath();
        XmlFactoryBuilder builder = new XmlFactoryBuilder(xmlPath);
        XmlFactoryBuilder.XmlFactory factory = builder.getFactory();
        return factory.getConfiguration();
    }
}
