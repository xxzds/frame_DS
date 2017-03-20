package com.anjz.spring;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 初始化时，将allowBeanDefinitionOverriding设置为false
 * 如果加载bean时，出现id相同的bean，会抛出异常 
 * @author shuai.ding
 * @date 2017年3月20日上午11:00:31
 */
public class SpringApplicationContextInitializer implements ApplicationContextInitializer<XmlWebApplicationContext> {

	public void initialize(XmlWebApplicationContext applicationContext) {
        //在这里将XmlWebApplicationContext属性allowBeanDefinitionOverriding设置为false,这个属
		//性的值最终会传递给DefaultListableBeanFactory类的allowBeanDefinitionOverriding属性
		applicationContext.setAllowBeanDefinitionOverriding(false);		
	}
}
	