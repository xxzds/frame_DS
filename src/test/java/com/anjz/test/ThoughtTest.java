package com.anjz.test;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 随想测试
 * @author ding.shuai
 * @date 2017年7月30日下午5:57:44
 */
public class ThoughtTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(ThoughtTest.class);

	@Test
	public void valueMinMaxTest(){	
		LOGGER.info("Byte min:{},max:{}",Byte.MIN_VALUE,Byte.MAX_VALUE);
		LOGGER.info("Short min:{},max:{}",Short.MIN_VALUE,Short.MAX_VALUE);
		LOGGER.info("Integer min:{},max:{}",Integer.MIN_VALUE,Integer.MAX_VALUE);
		LOGGER.info("Long min:{}, max:{}",Long.MIN_VALUE,Long.MAX_VALUE);
		
		
		LOGGER.info("Float min:{},max:{}",Float.MIN_VALUE,Float.MAX_VALUE);
		LOGGER.info("Double min:{},max:{}",Double.MIN_VALUE,Double.MAX_VALUE);
	}
}
