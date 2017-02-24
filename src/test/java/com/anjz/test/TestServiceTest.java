package com.anjz.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.anjz.BaseTest;
import com.anjz.core.service.intf.test.TestService;
import com.anjz.result.BaseResult;

/**
 * 测试各种异常
 * @author shuai.ding
 * @date 2017年2月24日上午10:37:17
 */
public class TestServiceTest extends BaseTest{
	@Resource
	private TestService testService;
	
	/**
	 * 业务异常测试
	 */
	@Test
	public void testBusinessExceptionTest(){
		BaseResult baseResult = testService.testBusinessException();
		logger.info(JSON.toJSONString(baseResult));
	}
	
	
	/**
	 * 运行时异常
	 */
	@Test
	public void testExceptionTest(){
		testService.testException();
	}
}
