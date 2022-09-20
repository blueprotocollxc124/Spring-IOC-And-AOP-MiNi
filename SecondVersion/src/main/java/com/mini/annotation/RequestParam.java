package com.mini.annotation;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */


import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
	  String value() default "";
}
