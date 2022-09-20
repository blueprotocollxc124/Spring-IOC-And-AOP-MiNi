package com.mini.aop;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/20
 */


import com.mini.factory.BeanFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkInvocationHandler implements InvocationHandler {
	  private Object target;
	  
	  
	  
	  public void setTarget(Object target) {
			this.target = target;
	  }
	  
	  public Object getProxy() {
			return Proxy.newProxyInstance(this.getClass().getClassLoader(),
					target.getClass().getInterfaces(), this);
	  }
	  
	  @Override
	  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			logPrint("调用了" + method.getName() + "方法！");
			Object result = method.invoke(target, args);
			return result;
	  }
	  
	  public void logPrint(String message) {
			LogAop logAop = (LogAop) BeanFactory.getBean("logAop");
			logAop.printLog(message);
	  }
}
