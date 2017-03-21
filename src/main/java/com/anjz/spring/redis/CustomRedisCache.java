package com.anjz.spring.redis;

import java.util.Set;
import java.util.concurrent.Callable;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

/**
 * 自定义redis缓存对象（org.springframework.cache.support.SimpleCacheManager）  
 *
 *规则：一个缓存名称下，key： 缓存名称$传入key
 *
 * @author ding.shuai
 * @date 2016年9月26日下午10:44:22
 */
public class CustomRedisCache implements Cache{
	
	/**
	 * redis
	 */
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 缓存名称 
	 */
	private String name;

	/**
	 * 超时时间
	 */
	private long timeout;
	
	/**
	 *  key前缀
	 */
	private String prefix;
	

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public Object getNativeCache() {
		 return this.redisTemplate;
	}


	@Override
	public ValueWrapper get(Object key) {
		if (StringUtils.isEmpty(key)) {  
            return null;  
        } else {  
            final String finalKey;  
            if (key instanceof String) {  
                finalKey =prefix+(String) key;  
            } else {  
                finalKey =prefix+ key.toString();  
            }  
            Object object = null;  
            object = redisTemplate.execute(new RedisCallback<Object>() {  
            	@Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {  
                    byte[] key = finalKey.getBytes();  
                    byte[] value = connection.get(key);  
                    if (value == null) {  
                        return null;  
                    }  
                    return SerializeUtils.deserialize(value);  
                }  
            });  
            return (object != null ? new SimpleValueWrapper(object) : null);  
        }  
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> type) {
		if (StringUtils.isEmpty(key) || null == type) {  
            return null;  
        } else {  
            final String finalKey;  
            final Class<T> finalType = type;   
            if (key instanceof String) {  
                finalKey =prefix+(String) key;  
            } else {  
                finalKey =prefix+ key.toString();  
            }  
            final Object object = redisTemplate.execute(new RedisCallback<Object>() {  
                public Object doInRedis(RedisConnection connection) throws DataAccessException {  
                    byte[] key = finalKey.getBytes();  
                    byte[] value = connection.get(key);  
                    if (value == null) {  
                        return null;  
                    }  
                    return SerializeUtils.deserialize(value);  
                }  
            });  
            if (finalType != null && finalType.isInstance(object) && null != object) {  
                return (T) object;  
            } else {  
                return null;  
            }  
        }  
	}

	@Override
	public void put(Object key, Object value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {  
            return;  
        } else {  
            final String finalKey;  
            if (key instanceof String) {  
                finalKey =prefix+(String) key;  
            } else {  
                finalKey =prefix+ key.toString();  
            }  
            if (!StringUtils.isEmpty(finalKey)) {  
                final Object finalValue = value;  
                redisTemplate.execute(new RedisCallback<Boolean>() {  
                    @Override  
                    public Boolean doInRedis(RedisConnection connection) {  
                        connection.set(finalKey.getBytes(), SerializeUtils.serialize(finalValue));  
                        // 设置超时间  
                        if(timeout!=0){
                        	 connection.expire(finalKey.getBytes(), timeout);  
                        }
                       
                        return true;  
                    }  
                });  
            }  
        }		
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 根据key删除缓存
	 */
	@Override
	public void evict(Object key) {
		if (null != key) {  
            final String finalKey;  
            if (key instanceof String) {  
                finalKey =prefix+(String) key;  
            } else {  
                finalKey =prefix+ key.toString();  
            }  
            if (!StringUtils.isEmpty(finalKey)) {  
                redisTemplate.execute(new RedisCallback<Long>() {  
                    public Long doInRedis(RedisConnection connection) throws DataAccessException {  
                        return connection.del(finalKey.getBytes());  
                    }  
                });  
            }  
        }		
	}

	/**
	 * 清除缓存名为name的缓存缓存
	 */
	@Override
	public void clear() {
		redisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection) throws DataAccessException {
				String keys=prefix+"*";				
				 Set<byte[]> keySet= connection.keys(SerializeUtils.serialize(keys));
				 for(byte[] key:keySet){
					 connection.del(key);  
				 }
				return "ok";
			}
		});		
	}

	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	public long getTimeout() {
		return timeout;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	public void setName(String name) {
		this.name = name;
		this.prefix=name+"$";
	}

	/**
	 * 4.3新增接口
	 */
	@Override
	public <T> T get(Object key, Callable<T> valueLoader) {		
		return null;
	}
}
