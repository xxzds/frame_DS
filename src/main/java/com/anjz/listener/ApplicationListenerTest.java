package com.anjz.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;



public class ApplicationListenerTest implements ApplicationListener<ContextRefreshedEvent>{

	private static final Logger logger =LoggerFactory.getLogger(ApplicationListenerTest.class);
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {		
		String name = event.getApplicationContext().getDisplayName();
		logger.info("---------------------------------------------------------");
		logger.info(name);
		logger.info("---------------------------------------------------------");
	}

}
