package com.anjz.redis;

import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.util.RedisUtil;

/**
 * http://www.runoob.com/redis/redis-sets.html
 * @author ding.shuai
 * @date 2017年7月30日上午12:00:29
 */
public class RedisSetUtilTest extends BaseTest{

	@Before
	public void addSetTest(){
		RedisUtil.addSet("redis-set", "1");
		RedisUtil.addSet("redis-set", "2");
		RedisUtil.addSet("redis-set", "3");
		RedisUtil.addSet("redis-set", "1");
		RedisUtil.addSet("redis-set", "4");
		RedisUtil.addSet("redis-set", (Object)new String[]{"1","2"});
	}
	
	@Test
	public void getSetTest(){
		Set<Object> set = RedisUtil.getSet("redis-set");
		logger.info("-----------------------------start-------------------");
		for(Object object:set){
			if(object instanceof String[]){
				logger.info("value={},is a array!",object);
			}
			logger.info("value={}",object);
		}
		logger.info("-----------------------------end-------------------");
	}
	
	@After
	public void delSetValueTest(){
//		RedisUtil.delSetValue("redis-set", new String[]{"1","2"});
//		RedisUtil.delSetValue("redis-set", "3","4");
		RedisUtil.delByKey("redis-set");
	}
}
