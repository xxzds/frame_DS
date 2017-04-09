package com.anjz.http;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.http.core.HttpUrlConnectionUtil;

/**
 * @author ding.shuai
 * @date 2017年4月9日下午4:53:08
 */
public class HttpUtilsTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtilsTest.class);
	
	@Test
	public void httpUrlConnectionUtilTest(){
		try{
			HttpUrlConnectionUtil.doPost("http://localhost:9999/kj/test/ajax3", "{\"name\":\"你好\"}", 60000);	
//			HttpUrlConnectionUtil.doGet("http://www.xh99d.com", null, 5000);
		}catch(Exception e){
			LOGGER.error("exception:",e);
		}
	}
}
