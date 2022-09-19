package com.mini.annotation;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */


import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
	  String value() default "";
}
