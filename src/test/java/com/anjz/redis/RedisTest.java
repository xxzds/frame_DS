package com.anjz.redis;

import java.util.List;
import java.util.Map;
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
import com.anjz.result.BaseResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 注意StringRedisSerializer和JdkSerializationRedisSerializer的区别
 * redis的五种类型 string，hash，set，zset,list
 * @author ding.shuai
 * @date 2016年9月27日上午1:30:06
 */
public class RedisTest extends BaseTest{
	
	@Resource
	private RedisTemplate<?, ?> redisTemplate;
	
	/**
	 * 获取redis中存储的信息
	 */
	@Test
	public void valuesTest(){
		  redisTemplate.execute(new RedisCallback<List<Object>>() {
			@Override
			public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
				String key="*";
				
				Set<byte[]> sets= connection.keys(new StringRedisSerializer().serialize(key));
				
				List<Object> result=Lists.newArrayList();
				logger.info("********************************start********************************");
				for(byte[] bytes:sets){
					try{
						Object value= new JdkSerializationRedisSerializer().deserialize(connection.get(bytes));
						
						result.add(value);
						
						logger.info("redis key="+new StringRedisSerializer().deserialize(bytes)+",value="+value);
					}catch(Exception e){
						logger.error("error exception",e);
						continue;
					}					
				}	
				logger.info("********************************end********************************");
				return result;
			}
		});
	}

	/**
	 * hash 操作
	 */
	@Test
	public void mapTest(){
		redisTemplate.execute(new RedisCallback<Boolean>() {

			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				
				StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
				JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
				
				byte[] key =stringRedisSerializer.serialize("test");
				Map<byte[],byte[]> hashes =Maps.newHashMap();
				hashes.put(stringRedisSerializer.serialize("name"), jdkSerializationRedisSerializer.serialize("丁帅"));
				hashes.put(stringRedisSerializer.serialize("object"), jdkSerializationRedisSerializer.serialize(new BaseResult()));
				logger.info("********************************start********************************");
				//创建
				connection.hMSet(key, hashes);
				
				//查询
//				List<byte[]> lists =  connection.hMGet(key,stringRedisSerializer.serialize("name"));
				Map<byte[],byte[]> hashesResult = connection.hGetAll(key);
				for(byte[] key1:hashesResult.keySet()){					
					logger.info("key="+stringRedisSerializer.deserialize(key1)+",value="+jdkSerializationRedisSerializer.deserialize(hashesResult.get(key1)));
				}
				
				//删除
				connection.del(key);
				logger.info("********************************end********************************");
				return null;	
			}
		});
	}
}
