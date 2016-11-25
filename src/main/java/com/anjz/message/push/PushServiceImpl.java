package com.anjz.message.push;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * 推送服务
 * @author ding.shuai
 * @date 2016年9月18日下午6:54:02
 */
public class PushServiceImpl implements PushService {
	private static final Logger logger=LoggerFactory.getLogger(PushServiceImpl.class);
	
	private volatile Map<String, Queue<DeferredResult<Object>>> userIdToDeferredResultMap = new ConcurrentHashMap<String, Queue<DeferredResult<Object>>>();

	public boolean isOnline(final String userId) {
		return userIdToDeferredResultMap.containsKey(userId);
	}

	/**
	 * 上线后 创建一个空队列，防止多次判断
	 * 
	 * @param userId
	 */
	public void online(final String userId) {
		Queue<DeferredResult<Object>> queue = userIdToDeferredResultMap.get(userId);
		if (queue == null) {
			queue = new LinkedBlockingDeque<DeferredResult<Object>>(); // 如果jdk 1.7 可以换成ConcurrentLinkedQueue
			userIdToDeferredResultMap.put(userId, queue);
		}
	}
	
	public void offline(final String userId) {
        Queue<DeferredResult<Object>> queue = userIdToDeferredResultMap.remove(userId);
        if(queue != null) {
            for(DeferredResult<Object> result : queue) {
                try {
                    result.setResult("");
                } catch (Exception e) {
                    //ignore
                }
            }
        }
    }
	
	public DeferredResult<Object> newDeferredResult(final String userId) {		
//		logger.info("用户["+userId+"]对应的队列中对象的个数:"+userIdToDeferredResultMap.get(userId).size());
		
        final DeferredResult<Object> deferredResult = new DeferredResult<Object>();
        //对于onCompletion回调,当异步请求完成时(包括超时和网络错误),这个方法将被调用
        deferredResult.onCompletion(new Runnable() {
            @Override
            public void run() {
                Queue<DeferredResult<Object>> queue = userIdToDeferredResultMap.get(userId);
                if(queue != null) {
                    queue.remove(deferredResult);
                    deferredResult.setResult("");
                }
            }
        });
        
        //超时的回调
        deferredResult.onTimeout(new Runnable() {
            @Override
            public void run() {
                deferredResult.setErrorResult("{}");
            }
        });
        Queue<DeferredResult<Object>> queue = userIdToDeferredResultMap.get(userId);
        if(queue == null) {
            queue = new LinkedBlockingDeque<DeferredResult<Object>>();
            userIdToDeferredResultMap.put(userId, queue);
        }
        queue.add(deferredResult);

        return deferredResult;
    }
	
	public void push(final String userId, final Object data) {
        Queue<DeferredResult<Object>> queue =  userIdToDeferredResultMap.get(userId);
        if(queue == null) {
            return;
        }
        for(DeferredResult<Object> deferredResult : queue) {
            if(!deferredResult.isSetOrExpired()) {
                try {
                    deferredResult.setResult(data);
                } catch (Exception e) {
                    queue.remove(deferredResult);
                }
            }
        }
    }
	
	
	/**
     * 定期清空队列 防止中间推送消息时中断造成消息丢失
     */
//    @Scheduled(fixedRate = 5L * 60 * 1000)
    public void sync() {
    	logger.info("定期清空队列 防止中间推送消息时中断造成消息丢失");
        Map<String, Queue<DeferredResult<Object>>> oldMap = userIdToDeferredResultMap;
        userIdToDeferredResultMap = new ConcurrentHashMap<String, Queue<DeferredResult<Object>>>();
        for(Queue<DeferredResult<Object>> queue : oldMap.values()) {
            if(queue == null) {
                continue;
            }

            for(DeferredResult<Object> deferredResult : queue) {
                try {
                    deferredResult.setResult("");
                } catch (Exception e) {
                    queue.remove(deferredResult);
                }
            }

        }
    }
}
