package com.anjz.redis;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.util.RedisUtil;

/**
 * @author ding.shuai
 * @date 2017年3月29日下午8:59:19
 */
public class RedisUtilTest extends BaseTest{

	@Before
	public void setHashTest(){
		RedisUtil.setHash("test", "name", "666");
		RedisUtil.setHash("test", "name2", "777");
		RedisUtil.setHash("test", "name3", "不错");
	}
	
	@Test
	public void getHashTest(){
		Map<String, Object> map = RedisUtil.getHash("test");
		logger.info("-----------------------------start-------------------");
		for(String key:map.keySet()){
			logger.info("key:{},value={}",key,map.get(key));
		}
		logger.info("-----------------------------end-------------------");
	}
	
	@After
	public void delHashByField(){
		RedisUtil.delHashByField("test", "name");
		RedisUtil.delHashByField("test", "name1");
		RedisUtil.delHashByField("test", "name2");
	}
}
