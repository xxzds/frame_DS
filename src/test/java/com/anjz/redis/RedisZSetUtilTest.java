package com.anjz.redis;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.util.RedisUtil;

/**
 * @author ding.shuai
 * @date 2017年7月30日下午5:36:36
 */
public class RedisZSetUtilTest extends BaseTest{

	@Before
	public void addZSet(){
		RedisUtil.addZSet("zset", 1, "nihao");
		RedisUtil.addZSet("zset", 2, "你好");
	}
	
	@Test
	public void getZSet(){
		try{
			Set<Object> set = RedisUtil.getZSet("zset");
			logger.info("-----------------------------start-------------------");
			for(Object object:set){
				logger.info("value={}",object);
			}
			logger.info("-----------------------------end-------------------");
		}catch(Exception e){
			logger.error("exception:",e);
		}		
	}
	
	@After
	public void del(){
		RedisUtil.delByKey("zset");
	}
}
