package com.mini.aop;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/20
 */


import org.apache.log4j.Logger;

public class LogAop {
	  public void printLog(String message) {
			Logger logger = Logger.getLogger(this.getClass());
			logger.info("输出日志:"+message);
	  }
}
