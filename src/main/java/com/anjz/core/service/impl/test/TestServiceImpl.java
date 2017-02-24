package com.anjz.core.service.impl.test;

import org.springframework.stereotype.Service;

import com.anjz.core.service.intf.test.TestService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;

/**
 * @author shuai.ding
 * @date 2017年2月24日上午10:33:29
 */
@Service
public class TestServiceImpl implements TestService{

	@Override
	public BaseResult testBusinessException() {	
		throw new BusinessException(1,"业务错误");
	}

	@Override
	public void testException() {		
		throw new RuntimeException("测试运行时异常");
	}

}
