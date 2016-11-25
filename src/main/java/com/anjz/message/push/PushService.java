package com.anjz.message.push;

import org.springframework.web.context.request.async.DeferredResult;

/**
 * @author ding.shuai
 * @date 2016年9月18日下午6:53:55
 */
public interface PushService {
	
	/**
	 * 判断用户是否在推送队列中
	 * @param userId
	 * @return
	 */
	public boolean isOnline(final String userId);
	
	
	/**
	 * 上线后 创建一个空队列，防止多次判断
	 * @param userId
	 */
	public void online(final String userId);
	
	/**
	 * 清除用户的队列
	 * @param userId
	 */
	public void offline(final String userId);
	
	/**
	 * 添加一个DeferredResult到队列中
	 * @param userId
	 * @return
	 */
	public DeferredResult<Object> newDeferredResult(final String userId);
	
	
	/**
	 * 将数据添加到队列中的DeferredResult对象中
	 * @param userId
	 * @param data
	 */
	public void push(final String userId, final Object data);
}
