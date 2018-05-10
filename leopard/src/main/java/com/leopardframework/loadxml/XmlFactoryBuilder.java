package com.leopardframework.loadxml;

import com.leopardframework.bean.BeanInfo;
import com.leopardframework.bean.BeanInvoke;
import com.leopardframework.bean.property.PropsInfo;
import com.leopardframework.logging.log.Log;
import com.leopardframework.logging.log.LogFactory;
import com.leopardframework.util.FileUtil;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright (c) 2018, Chen_9g 陈刚 (80588183@qq.com).
 * <p>
 * DateTime 2018/4/12
 * <p>
 * A novice on the road, please give me a suggestion.
 * 众里寻他千百度，蓦然回首，那人却在，灯火阑珊处。
 * Find a way for success and not make excuses for failure.
 * <p>
 * xml 的解析 将xml信息注入到相应的bean ...
 * 相当于Spring 的 ioc 依赖注入..
 */
public class XmlFactoryBuilder {

    private static final Log LOG = LogFactory.getLog(XmlFactoryBuilder.class);

    private Map<String, BeanInfo> beansMap; // 存放所有bean标签的信息

    private Map<String, Object> beansInstance = new HashMap<String, Object>(); // bean标签id bean实例

    private String entityPackage;  // 上下文信息 实体对象类所在包

    private String generatorPackage; //逆向工程生成JavaBean 包

    private String generatorProject; //工程路径

    public XmlFactory getFactory() {
        return new XmlFactory();
    }

    public XmlFactoryBuilder(String xmlpath) {
        if (!FileUtil.checkFileExists(xmlpath)) {
            throw new RuntimeException(xmlpath + " 文件没有找到...");
        }
        File file = FileUtil.createFile(xmlpath);
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();  //创建解析工厂
            SAXParser parser = factory.newSAXParser(); //解析对象创建
            parser.parse(file, new DefaultHandler() {

                private BeanInfo beanInfo;

                @Override    //文档开始
                public void startDocument() throws SAXException {
                    beansMap = new HashMap<String, BeanInfo>();  //创建beanmap
                }

                @Override    //标签开始
                public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {

                    if ("bean".equals(qName)) {
                        beanInfo = new BeanInfo();
                        beanInfo.setId(attrs.getValue("id"));
                        beanInfo.setClassName(attrs.getValue("class"));
                        beanInfo.setProps(new ArrayList<>());
                    } else if ("property".equals(qName)) {
                        beanInfo.getProps().add(new PropsInfo(attrs.getValue("name"),
                                attrs.getValue("value")));
                    } else if ("entity-package".equals(qName)) {
                        entityPackage = attrs.getValue("value");
                    } else if ("target".equals(qName)) {
                        generatorPackage = attrs.getValue("package");
                        generatorProject = attrs.getValue("project");
                    }
                }

                @Override    //标签结束
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if ("bean".equals(qName)) {
                        beansMap.put(beanInfo.getId(), beanInfo);  //标签结束添加进 map
                        beansInstance.put(beanInfo.getId(), instanceBean(beanInfo));
                        beanInfo = null;
                    }
                }

                @Override   //文档结束
                public void endDocument() throws SAXException {
                    super.endDocument();
                }
            });
        } catch (Exception e) {
            LOG.error("解析xml 出错了...", e);
            e.printStackTrace();
        }

    }

    /**
     * bean实例化
     *
     * @param beanInfo
     * @return
     */
    private Object instanceBean(BeanInfo beanInfo) {
        try {
            Class<?> cls = Class.forName(beanInfo.getClassName());
            Object object = cls.newInstance();
            for (PropsInfo pi : beanInfo.getProps()) {
                Field field = cls.getDeclaredField(pi.getName());
                if (field == null) {
                    LOG.error(" 没有" + pi.getName() + " 字段...");
                    continue;
                }
                BeanInvoke.invokeField(object, field, pi.getValue());
            }
            return object;

        } catch (Exception e) {
            LOG.error("bean注入失败...", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据bean的id将值注入
     */
    public class XmlFactory {
        public Object getBean(String id) {
            BeanInfo beanInfo = beansMap.get(id);
            return instanceBean(beanInfo);
        }

        public boolean hasBean(String className) {
            BeanInfo bean = beansMap.get("dataSource");

            if (bean == null) {
                throw new RuntimeException("配置文件中没有配置 dataSource...");
            }
            String cname = bean.getClassName();
            return className.equals(cname);
        }

        public String getEntityPackage() {
            return entityPackage;
        }

        public String getGeneratorPackage() {
            return generatorPackage;
        }

        public String getGeneratorProject() {
            return generatorProject;
        }
    }
}
