package com.anjz.redis;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
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
	
	private  static StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	
	/**
	 * 获取redis中存储的信息
	 */
	@Test
	public void valuesTest(){
		try{
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
		}catch(Exception e){
			logger.error("exception:",e);
		}
		  
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
	
	/**
	 * 订阅
	 * @throws IOException 
	 */
	@Test
	public void subscribe() throws IOException{
		final String[] channels = {"redisChat"};
		redisTemplate.execute(new RedisCallback<Boolean>() {
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[][] channelsByte=new byte[channels.length][];
				for(int i=0;i<channels.length;i++){
					String channel = channels[i];
					byte[] channelByte=stringRedisSerializer.serialize(channel);
					channelsByte[i]=channelByte;
				}
				
				connection.subscribe(new MessageListener() {					
					@Override
					public void onMessage(Message message, byte[] pattern) {						
						logger.info("pattern is {}",stringRedisSerializer.deserialize(pattern));
						logger.info("channel is {}",stringRedisSerializer.deserialize(message.getChannel()));
						logger.info("message body is {}",stringRedisSerializer.deserialize(message.getBody()));
					}
				}, channelsByte);
				return null;
			}
		});
		System.in.read();
	}
	
	/**
	 * 发布
	 * @throws IOException 
	 */
	@Test
	public void publish() throws IOException{
		redisTemplate.execute(new RedisCallback<Boolean>() {
			final String channel ="redisChat";
			@Override
			public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
				byte[] message = stringRedisSerializer.serialize("Redis is a great caching technique");			
				connection.publish(stringRedisSerializer.serialize(channel), message);
				
				return null;
			}
		});
//		System.in.read();
	}
}
