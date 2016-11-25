package com.anjz.spring;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.sf.ehcache.Ehcache;

/**
 * 桥接，给shiro提供缓存管理
 * @author ding.shuai
 * @date 2016年9月27日上午9:52:05
 */
public class SpringCacheManagerWrapper implements CacheManager {
	
	private static final Logger logger=LoggerFactory.getLogger(SpringCacheManagerWrapper.class);

	private org.springframework.cache.CacheManager cacheManager;

	/**
	 * 设置spring cache manager
	 *
	 * @param cacheManager
	 */
	public void setCacheManager(org.springframework.cache.CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Override
	public Cache<Object, Object> getCache(String name) throws CacheException {
		org.springframework.cache.Cache springCache = cacheManager.getCache(name);
		return new SpringCacheWrapper(springCache);
	}

	
	/**
	 * 内部类 shiro的缓存类
	 * @author ding.shuai
	 * @date 2016年9月27日上午9:51:38
	 */
	static class SpringCacheWrapper implements Cache<Object, Object> {
		private org.springframework.cache.Cache springCache;

		SpringCacheWrapper(org.springframework.cache.Cache springCache) {
			this.springCache = springCache;
		}

		@Override
		public Object get(Object key) throws CacheException {
			Object value = springCache.get(key);
			if (value instanceof SimpleValueWrapper) {
				return ((SimpleValueWrapper) value).get();
			}
			return value;
		}

		@Override
		public Object put(Object key, Object value) throws CacheException {
			springCache.put(key, value);
			return value;
		}

		@Override
		public Object remove(Object key) throws CacheException {
			springCache.evict(key);
			return null;
		}

		@Override
		public void clear() throws CacheException {
			springCache.clear();
		}

		@Override
		public int size() {
			if (springCache.getNativeCache() instanceof Ehcache) {
				Ehcache ehcache = (Ehcache) springCache.getNativeCache();
				return ehcache.getSize();
			}
			
			if(springCache.getNativeCache() instanceof RedisTemplate){
				RedisTemplate<?, ?> redisTemplate=(RedisTemplate<?, ?>)springCache.getNativeCache();
				return (int) redisTemplate.execute(new RedisCallback<Integer>() {
					@Override
					public Integer doInRedis(RedisConnection connection) throws DataAccessException {
						String keys=springCache.getName()+"$"+"*";				
						 Set<byte[]> keySet= connection.keys(new StringRedisSerializer().serialize(keys));
						 return keySet.size();
					}
				});
			}
			
			throw new UnsupportedOperationException("invoke spring cache abstract size method not supported");
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<Object> keys() {
			if (springCache.getNativeCache() instanceof Ehcache) {
				Ehcache ehcache = (Ehcache) springCache.getNativeCache();
				return new HashSet<Object>(ehcache.getKeys());
			}
			
			if(springCache.getNativeCache() instanceof RedisTemplate){
				RedisTemplate<?, ?> redisTemplate=(RedisTemplate<?, ?>)springCache.getNativeCache();
				return redisTemplate.execute(new RedisCallback<Set<Object>>() {
					@Override
					public Set<Object> doInRedis(RedisConnection connection) throws DataAccessException {
						String key=springCache.getName()+"$"+"*";
						
						Set<byte[]> sets= connection.keys(new StringRedisSerializer().serialize(key));
						
						Set<Object> result=Sets.newHashSet();
						for(byte[] bytes:sets){
							Object keyO= new StringRedisSerializer().deserialize(bytes);
							if(keyO!=null){
								String keyS=(String)keyO;
								String[] keyA = keyS.split("\\$");
								result.add(keyA.length>1?keyA[keyA.length-1]:keyO);
								logger.info("redis key:"+keyO);
							}							
						}						
						return result;
					}
				});
			}
			throw new UnsupportedOperationException("invoke spring cache abstract keys method not supported");
		}

		@Override
		public Collection<Object> values() {
			if (springCache.getNativeCache() instanceof Ehcache) {
				Ehcache ehcache = (Ehcache) springCache.getNativeCache();
				List<?> keys = ehcache.getKeys();
				if (!CollectionUtils.isEmpty(keys)) {
					List<Object> values = new ArrayList<Object>(keys.size());
					for (Object key : keys) {
						Object value = get(key);
						if (value != null) {
							values.add(value);
						}
					}
					return Collections.unmodifiableList(values);
				} else {
					return Collections.emptyList();
				}
			}
			
			if(springCache.getNativeCache() instanceof RedisTemplate){
				RedisTemplate<?, ?> redisTemplate=(RedisTemplate<?, ?>)springCache.getNativeCache();
				return redisTemplate.execute(new RedisCallback<Collection<Object>>() {
					@Override
					public Collection<Object> doInRedis(RedisConnection connection) throws DataAccessException {
						String key=springCache.getName()+"$"+"*";
						
						Set<byte[]> sets= connection.keys(new StringRedisSerializer().serialize(key));
						
						List<Object> result=Lists.newArrayList();
						for(byte[] bytes:sets){
							Object value= new JdkSerializationRedisSerializer().deserialize(connection.get(bytes));
							
							result.add(value);
							logger.info("redis key="+new StringRedisSerializer().deserialize(bytes)+",value="+value);
						}						
						return result;
					}
				});
			}
			throw new UnsupportedOperationException("invoke spring cache abstract values method not supported");
		}
	}
}
