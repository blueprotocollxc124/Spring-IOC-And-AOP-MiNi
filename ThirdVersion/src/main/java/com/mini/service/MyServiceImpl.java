package com.mini.service;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */

import com.mini.annotation.Service;
import com.mini.mapper.CountMapper;

@Service
public class MyServiceImpl implements MyService {
	  
	  private CountMapper countMapper;
	  
	  @Override
	  public String getName(String name) {
			return "My Name Is "+ name;
	  }
	  
	  @Override
	  public Integer getCount() {
			return countMapper.getAccount(1);
	  }
	  
	  
	  public void setCountMapper(CountMapper countMapper) {
	  	  this.countMapper = countMapper;
	  }
}
