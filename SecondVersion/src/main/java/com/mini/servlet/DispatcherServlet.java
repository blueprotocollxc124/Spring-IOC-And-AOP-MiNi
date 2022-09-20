package com.mini.servlet;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */


import com.mini.annotation.Autowire;
import com.mini.annotation.Controller;
import com.mini.annotation.Service;
import com.mini.factory.BeanFactory;
import com.mini.service.MyService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DispatcherServlet extends HttpServlet {
	  
	  private Properties properties = new Properties();
	  private Map<String, Object> beanMap = new ConcurrentHashMap<>();
	  private List<String> classNames = new ArrayList<String>();
	  
	  @Override
	  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			this.doPost(req, resp);
	  }
	  
	  @Override
	  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			System.out.println("调用doPost开始");
			MyService myService = (MyService) BeanFactory.getBean("myService");
			System.out.println("Count:"+myService.getCount());
	  }
	  
	  @Override
	  public void init(ServletConfig config) throws ServletException {
			// 开始初始化
			// 定位
			doLoadConfiguration(config.getInitParameter("ContextParam"));
			// 扫描
			doScan(properties.getProperty("scanPackage"));
			// 注册
			doRegister();
			// 自动依赖注入
			// 在Spring中是通过调用getBean方法才出发依赖注入的
			doAutowired();
			// SpringIOCController controller = (SpringIOCController)beanMap.get("springIOCController");
			
			
			// 如果是SpringMVC会多设计一个HandlerMapping
			// 将@RequestMapping中配置的url和一个Method关联上
			// 以便于从浏览器获得用户输入的url以后，能够找到具体执行的Method通过反射去调用
			initHandlerMapping();
			
			
	  }
	  
	  
	  public void doLoadConfiguration(String location) {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(location.replace("classpath:", ""));
			try {
				  properties.load(inputStream);
			} catch (IOException e) {
				  e.printStackTrace();
			}
	  }
	  
	  
	  private void doScan(String packageName) {
			URL url = this.getClass().getClassLoader().getResource("/" + packageName.replace(".", "/"));
			File classDir = new File(url.getFile());
			for (File file : classDir.listFiles()) {
				  if (file.isDirectory()) {
						doScan(packageName + "." + file.getName());
				  } else {
						classNames.add(packageName + "." + file.getName().replace(".class", ""));
				  }
			}
	  }
	  
	  private void doRegister() {
			if (classNames.isEmpty()) {
				  return;
			}
			for (String className : classNames) {
				  try {
						Class<?> aClass = Class.forName(className);
						// 在Spring中用多个子方法来处理
						if (aClass.isAnnotationPresent(Controller.class)) {
							  String beanName = lowerFirstCase(aClass.getSimpleName());
							  // 在Spring中在这个阶段不是不会直接put instance，这里put的是BeanDefinition
							  beanMap.put(beanName, aClass.getDeclaredConstructor().newInstance());
							  
						} else if (aClass.isAnnotationPresent(Service.class)) {
							  Service service = aClass.getAnnotation(Service.class);
							  String beanName = service.value();
							  // 默认用类名首字母注入
							  // 如果自己定义了beanName，那么优先使用自己定义的beanName
							  // 如果是一个接口，使用接口的类型去自动注入
							  
							  if ("".equals(beanName.trim())) {
									beanName = lowerFirstCase(aClass.getSimpleName());
							  }
							  Object instance = aClass.getDeclaredConstructor().newInstance();
							  beanMap.put(beanName, instance);
							  
							  Class<?>[] interfaces = aClass.getInterfaces();
							  
							  for (Class<?> anInterface : interfaces) {
									beanMap.put(anInterface.getName(), instance);
							  }
						} else {
							  continue;
						}
				  } catch (Exception exception) {
						exception.printStackTrace();
				  }
			}
	  }
	  
	  
	  private void doAutowired() {
			if (beanMap.isEmpty()) {
				  return;
			}
			for (Map.Entry<String, Object> entry : beanMap.entrySet()) {
				  Field[] fields = entry.getValue().getClass().getDeclaredFields();
				  for (Field field : fields) {
						if (!field.isAnnotationPresent(Autowire.class)) {
							  continue;
						}
						Autowire autowire = field.getAnnotation(Autowire.class);
						String beanName = autowire.value().trim();
						if ("".equals(beanName)) {
							  beanName = field.getType().getName();
						}
						field.setAccessible(true);
						try {
							  field.set(entry.getValue(), beanMap.get(beanName));
						} catch (IllegalAccessException e) {
							  e.printStackTrace();
						}
				  }
			}
	  }
	  
	  
	  private void initHandlerMapping() {
			System.out.println("@Interface---beanMap:");
			Set<Map.Entry<String, Object>> entries = beanMap.entrySet();
			for (Map.Entry<String, Object> entry : entries) {
				  System.out.println("key:"+entry.getKey()+"----value:"+entry.getValue());
			}
//			System.out.println("className:");
//			for (String className : classNames) {
//				  System.out.println(className);
//			}
			System.out.println("xml----beanMap:");
			Set<Map.Entry<String, Object>> entries1 = BeanFactory.beanMap.entrySet();
			for (Map.Entry<String, Object> stringObjectEntry : entries1) {
				  System.out.println("key:"+stringObjectEntry.getKey()+"----value:"+stringObjectEntry.getValue());
			}
	  }
	  
	  
	  private String lowerFirstCase(String str) {
			char[] chars = str.toCharArray();
			chars[0] += 32;
			return chars.toString();
	  }
}
