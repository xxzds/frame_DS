package com.anjz.redis;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.util.RedisUtil;

/**
 * @author ding.shuai
 * @date 2017年7月30日下午5:00:17
 */
public class RedisListUtilTest extends BaseTest{

	@Before
	public void addListTest(){
		RedisUtil.addList("list", "1","2","3",(Object)new String[]{"1","2"});
	}
	
	@Test
	public void getListTest(){
		List<Object> list = RedisUtil.getList("list");
		logger.info("------------------start------------------");
		for(Object object:list){
			logger.info("value={}",object);
		}
		logger.info("------------------end------------------");
	}
	
	@After
	public void del(){
		RedisUtil.delByKey("list");
	}
}
