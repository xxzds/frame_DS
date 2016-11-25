package com.anjz.message.push;

import java.util.Map;

import javax.annotation.Resource;

import com.google.common.collect.Maps;

/**
 * 将推送的数据添加到推送的队列中
 * @author ding.shuai
 * @date 2016年9月18日下午7:41:30
 */
public class PushApiImpl implements PushApi{
	
	@Resource
	private PushService pushService;
	

	@Override
	public void pushUnreadMessage(String userId, Long unreadMessageCount) {
		Map<String, Object> data = Maps.newHashMap();
        data.put("unreadMessageCount", unreadMessageCount);
        pushService.push(userId, data);		
	}

	@Override
	public void offline(String userId) {
		 pushService.offline(userId);		
	}

}
