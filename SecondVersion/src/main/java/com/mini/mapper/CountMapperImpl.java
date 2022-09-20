package com.mini.mapper;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/20
 */


public class CountMapperImpl implements CountMapper{
 
 @Override
 public Integer getAccount(int account) {
  return  Integer.valueOf(account);
 }
}
