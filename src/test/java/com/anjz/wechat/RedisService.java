package com.anjz.wechat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericToStringSerializer;

public class RedisService {
	
	private RedisTemplate<Serializable, Serializable> redisTemplate;
	
	
	/**
	 * 业务方法，可根据业务需要修改或添加
	 * 1.获取接口凭证
	 * */
	public String generateToken(String accessToken){
		String shid = "WX_ACCESS_TOKEN";
		getValueRedis().set(shid, accessToken, 3600, TimeUnit.SECONDS);//accessToken有效期为7200秒
		return accessToken;
	}
	
	
	/**
	 * 以下为redis基本方法，勿动
	 * */
	public RedisTemplate<Serializable, Serializable> getRedisTemplate() {
		return redisTemplate;
	}
	public void setRedisTemplate(
			RedisTemplate<Serializable, Serializable> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public boolean set(final String prefix, final String key, final Object obj,
			final long seconds) {

		// key的组织形式 namespace:id:parmeter
		redisTemplate.execute(new RedisCallback<Serializable>() {

			
			public Serializable doInRedis(final RedisConnection connection)
					throws DataAccessException {
				final byte[] keyBytes = SerializeUtil.serialize(prefix + ":"
						+ key);
				final byte[] name = SerializeUtil.serialize(obj);
				connection.setEx(keyBytes, seconds, name);
				return null;
			}
		});
		return true;
	}

	/**
	 * 根据key从redis里面取出value
	 * 
	 * @param key
	 *            key
	 */
	
	public Serializable get(final String prefix, final Serializable key) {
		return redisTemplate.execute(new RedisCallback<Serializable>() {
			
			public Serializable doInRedis(RedisConnection connection)
					throws DataAccessException {

				byte[] keyBytes = SerializeUtil.serialize(prefix + ":" + key);
				byte[] bytes = connection.get(keyBytes);
				return (Serializable) SerializeUtil.unserialize(bytes);
			}
		});
	}

	
	public boolean delete(final String prefix,final Serializable key) {
		redisTemplate.delete(prefix + ":" + key);
		return true;
	}
	
	public boolean deleteAll(final String prefix,final List<Serializable> keys) {
		List<Serializable> keyTypes = new ArrayList<Serializable>();
		for(Serializable ser : keys){
			keyTypes.add(prefix + ":" + ser);
		}
		redisTemplate.delete(keyTypes);
		return true;
	}
	
	public HashOperations<Serializable, Object, Object> getHashRedis(){
		return redisTemplate.opsForHash();
	}
	
	public ListOperations<Serializable, Serializable> getListRedis(){
		return redisTemplate.opsForList();
	}
	
	public SetOperations<Serializable, Serializable> getSetRedis(){
		return redisTemplate.opsForSet();
	}
	
	public ValueOperations<Serializable, Serializable> getValueRedis(){
		return redisTemplate.opsForValue();
	}
	
	public ZSetOperations<Serializable, Serializable> getZSetRedis(){
		return redisTemplate.opsForZSet();
	}
	/**
	 * 设置hashValue的Long二进制编码方式
	 */
	public void setLongHashValueSerializer(){
		redisTemplate.setHashValueSerializer(new GenericToStringSerializer<Long>(Long.class));
	}
}

