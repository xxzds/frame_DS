package com.anjz.core.controller.monitor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * jvm监控
 * 
 * @author ding.shuai
 * @date 2016年9月23日下午11:13:15
 */
@Controller
@RequestMapping("/monitor/jvm")
@RequiresPermissions("monitor:jvm:*")
public class JvmMonitorController {
	@RequestMapping("")
	public String index() {
		return "monitor/jvm/index";
	}
}
