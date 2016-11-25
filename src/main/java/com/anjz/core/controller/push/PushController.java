package com.anjz.core.controller.push;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.anjz.base.bind.annotation.CurrentUser;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.personal.PersonalMessageService;
import com.anjz.message.push.PushService;
import com.google.common.collect.Maps;

/**
 * 实时推送用户：消息和通知
 * 
 * @author ding.shuai
 * @date 2016年8月24日下午5:08:25
 */

@Controller
public class PushController {
	
	@Resource
	private PushService  pushService;
	
	@Resource
	private PersonalMessageService messageService;
	
	
	@RequestMapping(value = "/admin/polling")
	@ResponseBody
	public Object polling(HttpServletResponse resp, @CurrentUser SysUser user) {
		resp.setHeader("Connection", "Keep-Alive");
        resp.addHeader("Cache-Control", "private");
        resp.addHeader("Pragma", "no-cache");
        
        String userId = user.getId();
        if(userId == null) {
            return null;
        }
        
        if(!pushService.isOnline(userId)){
        	//第一次从数据库中读取
        	 long unreadMessageCount = messageService.countUnread(userId);
        	
        	 Map<String, Object> data = Maps.newHashMap();
             data.put("unreadMessageCount", unreadMessageCount);
        	 pushService.online(userId);
             return data;
        }else{        	
        	DeferredResult<Object> result= pushService.newDeferredResult(userId);
        	return result;
        }
	}
}
