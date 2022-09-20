package com.mini.controller;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */

import com.mini.annotation.Autowire;
import com.mini.annotation.Controller;
import com.mini.annotation.RequestMapping;
import com.mini.annotation.RequestParam;
import com.mini.service.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SpringIOCController {
	  
	  
	  @Autowire
	  private MyService myService;
	  
	  
	  @RequestMapping("/iocTest")
	  public void IOCTest(HttpServletRequest request, HttpServletResponse response, @RequestParam String name) {
			String DIName = myService.getName(name);
			System.out.println(DIName);
	  }
}
