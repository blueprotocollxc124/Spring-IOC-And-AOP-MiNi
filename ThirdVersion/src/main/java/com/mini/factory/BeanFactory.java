package com.mini.factory;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/20
 */


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanFactory {
	  
	  public static Map<String, Object> beanMap = new ConcurrentHashMap<>();
	  
	  
	  static {
	  
			// 加载xml文件
			InputStream inputStream = BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml");
			// 解析xml文件
			SAXReader reader = new SAXReader();
			
			try {
				  // 获取到xml文档
				  Document document = reader.read(inputStream);
				  // 拿到根元素
				  Element root = document.getRootElement();
				  // 搜索当前标签(root当前根标签beans)，下的所有bean标签
				  List<Element> list1 = root.selectNodes("bean");
				  for (Element element : list1) {
						String id = element.attributeValue("id");
						String absoluteClassName = element.attributeValue("class");
						Class<?> forNameClass = Class.forName(absoluteClassName);
						Object instance = forNameClass.getDeclaredConstructor().newInstance();
						beanMap.put(id,instance);
				  }
				  List<Element> propertyList = root.selectNodes("//property");
				  for (Element element : propertyList) {
						String name = element.attributeValue("name");
						String ref = element.attributeValue("ref");
						Element parentElement = element.getParent();
						String parentId = parentElement.attributeValue("id");
						Object parentObj = beanMap.get(parentId);
						Method[] methods = parentObj.getClass().getMethods();
						System.out.println(Arrays.toString(methods) );
						for (Method method : methods) {
							  if (("set"+firstToUp(name)).equals(method.getName())) {
							  	  method.invoke(parentObj,beanMap.get(ref));
							  	  break;
							  }
						}
						
						
						beanMap.put(parentId,parentObj);
				  }
			} catch (Exception e) {
				  e.printStackTrace();
			}
	  }
	  
	  
	  public static Object getBean(String id) {
			Object obj = beanMap.get(id);
			if (obj == null) {
				  throw new NullPointerException("beanMap中不存在该实例");
			}
			return obj;
	  }
	  
	  
	  private static String firstToUp(String str) {
			String s = String.valueOf(str.toCharArray()[0]);
			String upperCase = s.toUpperCase(Locale.ROOT);
			char[] upperCaseChar = upperCase.toCharArray();
			StringBuilder builder = new StringBuilder(str);
			builder.setCharAt(0, upperCaseChar[0]);
			return builder.toString();
	  }
	  
	  
}
