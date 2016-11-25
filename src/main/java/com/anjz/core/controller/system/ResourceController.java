package com.anjz.core.controller.system;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anjz.base.controller.BaseTreeableController;
import com.anjz.core.model.SysResource;

/**
 * 资源
 * 
 * @author ding.shuai
 * @date 2016年8月27日上午10:32:44
 */
@Controller
@RequestMapping("/system/resource")
public class ResourceController extends BaseTreeableController<SysResource, String> {

	public ResourceController() {
		setResourceIdentity("system:resource");
	}

}
