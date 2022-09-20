# SpringMiNi-IOC-And-AOP
基于Tomcat容器，Servlet技术来做一个简易版本的Spring IOC和AOP,目的是为了搞清楚其原理与提高自己的编码技巧。

第一个版本是注解的方式利用Java的反射机制进行DI的

第二个版本在第一个版本的基础上，在项目中引入了dom4j和jaxen来操作xml文件，通过解析xml文件中的标签和Java的反射机制进行动态DI，使用了工厂模式来获取bean。
