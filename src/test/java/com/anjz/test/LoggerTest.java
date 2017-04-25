package com.anjz.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志级别测试
 * @author shuai.ding
 *
 * @date 2017年4月24日下午4:47:52
 */
public class LoggerTest {

	private static final Logger logger = LoggerFactory.getLogger(LoggerTest.class);
	
	@Test
	public void loggerTest(){
		logger.trace("-------------trace-------------");
		logger.debug("-------------debug-------------");
		logger.info("-------------info-------------");
		logger.warn("-------------warn-------------");
		logger.error("-------------error-------------");
	}
}
