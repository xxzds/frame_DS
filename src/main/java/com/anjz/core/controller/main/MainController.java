package com.anjz.core.controller.main;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.bind.annotation.CurrentUser;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.personal.PersonalMessageService;
import com.anjz.core.service.intf.system.SysResourceService;
import com.anjz.core.vo.Menu;
import com.anjz.message.push.PushApi;
import com.anjz.result.ListResult;

/**
 * @author ding.shuai
 * @date 2016年8月19日下午5:22:02
 */

@Controller
public class MainController {

	@Resource
	private SysResourceService sysResourceService;
	
	@Resource
	private PushApi pushApi;
	
	@Resource
	private PersonalMessageService personalMessageService;

	@RequestMapping("/")
	public String main(@CurrentUser SysUser user,Model model) {
		
		ListResult<Menu> result= sysResourceService.findMenus(user);
		if(result.isSuccess()){
			 model.addAttribute("menus", result.getData());
		}
		
		//清除队列中的数据
		pushApi.offline(user.getId());
		
		return "main/index";
	}

	@RequestMapping(value = "/welcome")
	public String welcome(@CurrentUser SysUser user,Model model) {
		
		//未读消息
		long unread=personalMessageService.countUnread(user.getId());
		model.addAttribute("messageUnreadCount", unread);
		
		return "main/welcome";
	}

}
