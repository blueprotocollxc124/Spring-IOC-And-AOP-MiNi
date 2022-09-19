package com.mini.service;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */

import com.mini.annotation.Service;

@Service
public class MyServiceImpl implements MyService {
	  @Override
	  public String getName(String name) {
			return "My Name Is "+ name;
	  }
}
