package com.mini.annotation;

/*
 *@Author  LXC BlueProtocol
 *@Since   2022/9/19
 */


import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
	  String value() default "";
}
