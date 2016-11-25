package com.anjz.core.controller.system;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.controller.BaseCRUDController;
import com.anjz.core.enums.AvailableEnum;
import com.anjz.core.model.SysPermission;
import com.anjz.core.service.intf.system.SysPermissionService;
import com.anjz.result.BaseResult;

/**
 * 权限列表模块
 * @author ding.shuai
 * @date 2016年9月7日上午11:39:40
 */
@Controller
@RequestMapping("/system/permission/permission")
public class PermissionController extends BaseCRUDController<SysPermission, String>{
	
	@Resource
	private SysPermissionService sysPermissionService;
	
	public PermissionController() {
		setResourceIdentity("system:permission");
	}
	
	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("availableList", AvailableEnum.values());
		super.setCommonData(model);
		
		//设置默认值
		SysPermission permission=new SysPermission();
		permission.setIsShow(true);
		model.addAttribute("m", permission);
	}

	/**
	 * 状态改变
	 * @param request
	 * @param ids
	 * @param newStatus
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("/changeStatus/{newStatus}")
	public String changeStatus( HttpServletRequest request,
			 @RequestParam("ids") String[] ids,
			  @PathVariable("newStatus") Boolean newStatus,
			  RedirectAttributes redirectAttributes){
		
		this.permissionList.assertHasUpdatePermission();
		
		for(String id:ids){
			SysPermission permission=new SysPermission();
			permission.setId(id);
			permission.setIsShow(newStatus);
			BaseResult result= sysPermissionService.updateSelectiveById(permission);
			if(!result.isSuccess()){
				redirectAttributes.addFlashAttribute(Constants.MESSAGE, result.getMessage());
				break;
			}
		}
		return redirectToUrl((String)request.getAttribute(Constants.BACK_URL));
	}

}
