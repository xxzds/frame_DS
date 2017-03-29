package com.anjz.util;

import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.anjz.spring.SpringUtils;
import com.google.common.collect.Maps;

/**
 * reids 工具类
 * @author ding.shuai
 * @date 2017年3月29日下午8:48:37
 */
public class RedisUtil {

	private static RedisTemplate<?, ?> redisTemplate = SpringUtils.getBean("redisTemplate");	
	private  static StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	private static JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
	
	/**
	 * 将field,value存入hash
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static  boolean setHash(final String key, final String field,final Object value){
    	return redisTemplate.execute(new RedisCallback<Boolean>(){
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {								
				return connection.hSet(stringRedisSerializer.serialize(key), stringRedisSerializer.serialize(field), jdkSerializationRedisSerializer.serialize(value));
			}
    	});
    }
	
	
	/**
	 * 获取hash集合
	 * @param key
	 * @return
	 */
	public static  Map<String, Object> getHash(final String key){
    	return redisTemplate.execute(new RedisCallback<Map<String, Object>>(){
			@Override
			public Map<String, Object> doInRedis(RedisConnection connection) throws DataAccessException {	
				Map<byte[], byte[]> map = connection.hGetAll(stringRedisSerializer.serialize(key));
				Map<String, Object> result =Maps.newHashMap();
				if(map!=null && map.size()>0){
					for(byte[] b:map.keySet()){
						result.put(stringRedisSerializer.deserialize(b), jdkSerializationRedisSerializer.deserialize(map.get(b)));
					}
				}
				return result;
			}
    	});
    }
	
	/**
	 * 将field,value存入hash
	 * 将存入hash中key为field的值删除
	 * @param key
	 * @param field
	 * @param value
	 * @return
	 */
	public static  boolean delHashByField(final String key, final String field){
    	return redisTemplate.execute(new RedisCallback<Boolean>(){
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {								
			    connection.hDel(stringRedisSerializer.serialize(key), stringRedisSerializer.serialize(field));
				return true;
			}
    	});
    }
}
