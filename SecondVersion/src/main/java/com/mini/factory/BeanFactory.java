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
				  List<Element> list = root.selectNodes("bean");
				  for (Element element : list) {
						// 获取每一个bean的id
						String id = element.attributeValue("id");
						// 获取每一个bean的全限定类名
						String absoluteClassName = element.attributeValue("class");
						Class<?> forNameClass = Class.forName(absoluteClassName);
						Object instance = forNameClass.getDeclaredConstructor().newInstance();
						System.out.println(id + ":" + instance);
						beanMap.put(id, instance);
						
						// 实例化完成后，维护对象的依赖关系，查询那些对象需要传值，根据配置，传入相应的值
						
						// 拿到所有的property配置对象
						List<Element> propertyList = root.selectNodes("//property");
						// 解析property，获取父元素
						for (Element property : propertyList) {
							  String name = property.attributeValue("name");
							  String ref = property.attributeValue("ref");
							  
							  // 获取父节点
							  Element propertyParent = property.getParent();
							  // 根据父节点的id，从我们的容器中拿
							  String parentId = propertyParent.attributeValue("id");
							  Object parentObj = beanMap.get(parentId);
							  // 通过反射对象所有方法，找到set+name ===>>> setCountMapper的方法
							  Method[] methods = parentObj.getClass().getMethods();
							  for (Method method : methods) {
									// 如果是上述的方法
									if (("set" + firstToUp(name)).equals(method.getName())) {
										  // 执行这个方法并传参数，ref就是另一个bean实例的id
										  method.invoke(parentObj, beanMap.get(ref));
										  break;
									}
							  }
							  // 处理完对象之后，将对象重新存放到我们的容器中
							  beanMap.put(parentId, parentObj);
						}
						
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
