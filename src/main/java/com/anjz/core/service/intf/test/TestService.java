package com.anjz.core.service.intf.test;

import com.anjz.result.BaseResult;

/**
 * 测试服务
 * @author shuai.ding
 * @date 2017年2月24日上午10:30:55
 */
public interface TestService{
	
	public BaseResult testBusinessException();
	
	public void testException();
}
