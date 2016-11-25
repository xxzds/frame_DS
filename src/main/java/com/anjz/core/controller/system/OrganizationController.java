package com.anjz.core.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.controller.BaseTreeableController;
import com.anjz.core.model.SysOrganization;

/**
 * 组织机构列表
 * @author ding.shuai
 * @date 2016年9月2日下午4:54:24
 */
@Controller
@RequestMapping("/system/organization")
public class OrganizationController extends BaseTreeableController<SysOrganization, String>{
	
	public OrganizationController() {
		setResourceIdentity("system:organization");
	}
}
