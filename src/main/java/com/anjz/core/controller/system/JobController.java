package com.anjz.core.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.controller.BaseTreeableController;
import com.anjz.core.model.SysJob;

/**
 * 工作职务列表
 * @author ding.shuai
 * @date 2016年9月2日下午5:39:21
 */
@Controller
@RequestMapping("/system/job")
public class JobController extends BaseTreeableController<SysJob, String>{

	public JobController() {
		setResourceIdentity("system:job");
	}
}
