package com.anjz.redis;

import org.junit.Before;
import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.util.RedisUtil;

/**
 * @author ding.shuai
 * @date 2017年7月30日下午4:21:18
 */
public class RedisStringUtilTest extends BaseTest{

	@Before
	public void setStringTest(){
		RedisUtil.setString("abcefg", "hehe1", 60);
	}
	
	@Test
	public void getStringTest(){
		String value = RedisUtil.getString("abcefg");
		logger.info("---------------------start---------------------");
		logger.info(value);
		logger.info("---------------------end---------------------");
	}
}
