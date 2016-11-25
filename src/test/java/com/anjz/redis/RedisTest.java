package com.anjz.redis;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.anjz.BaseTest;
import com.google.common.collect.Lists;

/**
 * @author ding.shuai
 * @date 2016年9月27日上午1:30:06
 */
public class RedisTest extends BaseTest{
	
	@Resource
	private RedisTemplate<?, ?> redisTemplate;
	
	@Test
	public void valuesTest(){
		  redisTemplate.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
				String key="*";
				
				Set<byte[]> sets= connection.keys(new StringRedisSerializer().serialize(key));
				
				List<Object> result=Lists.newArrayList();
				for(byte[] bytes:sets){
					try{
						Object value= new JdkSerializationRedisSerializer().deserialize(connection.get(bytes));
						
						result.add(value);
						logger.info("redis key="+new JdkSerializationRedisSerializer().deserialize(bytes)+",value="+value);
					}catch(Exception e){
						continue;
					}
					
				}						
				return result;
			}
		});
	}

}
