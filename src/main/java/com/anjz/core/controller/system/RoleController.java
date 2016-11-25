package com.anjz.core.controller.system;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.PageableDefaults;
import com.anjz.base.controller.BaseController;
import com.anjz.base.controller.permission.PermissionList;
import com.anjz.base.entity.search.Searchable;
import com.anjz.core.enums.AvailableEnum;
import com.anjz.core.model.SysRole;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.core.service.intf.system.SysPermissionService;
import com.anjz.core.service.intf.system.SysRoleResourcePermissionService;
import com.anjz.core.service.intf.system.SysRoleService;
import com.anjz.result.BaseResult;
import com.google.common.collect.Lists;

/**
 * 授权权限给角色
 * @author ding.shuai
 * @date 2016年9月7日下午12:30:06
 */
@Controller
@RequestMapping("/system/permission/role")
public class RoleController extends BaseController<SysRole, String>{
	protected PermissionList permissionList = null;
	
	@Resource
	private SysRoleService sysRoleService;
	
	@Resource
	private SysRoleResourcePermissionService sysRoleResourcePermissionService;
	
	@Resource
	private SysPermissionService sysPermissionService;

	/**
	 * 权限前缀：如sys:user 则生成的新增权限为 sys:user:create
	 */
	public void setResourceIdentity(String resourceIdentity) {
		if (!StringUtils.isEmpty(resourceIdentity)) {
			permissionList = PermissionList.newPermissionList(resourceIdentity);
		}
	}
	
	public RoleController() {
		setResourceIdentity("system:role");
	}
	
	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("availableList", AvailableEnum.values());
		super.setCommonData(model);
	}
	
	/**
	 * 模块中的主页
	 * 
	 * @param searchable
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "", "list" }, method = RequestMethod.GET)
	@PageableDefaults(sort = "id=desc")
	public String list(Searchable searchable, Model model) {

		if (permissionList != null) {
			this.permissionList.assertHasViewPermission();
		}

		model.addAttribute("page", sysRoleService.findAll(searchable));
		return viewName("list");
	}
	
	/**
     * 仅返回表格数据
     *
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(value = { "", "list" },method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String listTable(Searchable searchable, Model model) {
        list(searchable, model);
        return viewName("listTable");
    }
	
	
	/**
	 * 添加
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "create", method = RequestMethod.GET)
	public String showCreateForm(Model model) {

		if (permissionList != null) {
			this.permissionList.assertHasCreatePermission();
		}

		setCommonData(model);
		model.addAttribute(Constants.OP_NAME, "新增");
		if (!model.containsAttribute("m")) {
			// 设置默认数据
			SysRole role = new SysRole();
			role.setIsShow(true);
			model.addAttribute("m", role);
		}
		// 所有权限
		model.addAttribute("permissions", sysPermissionService.findAll().getData());

		return viewName("editForm");
	}
	
	@RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(
            Model model,  @ModelAttribute("m") SysRole m, BindingResult result,
            @RequestParam("resourceId") String[] resourceIds,
            @RequestParam("permissionIds") String[][] permissionIds,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }

        if (hasError(m, result)) {
            return showCreateForm(model);
        }
        BaseResult baseResult=sysRoleService.saveRoleAndRoleResourcePermissions(m, this.consivitionRoleResourcePermission(resourceIds, permissionIds));
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
        }       
        return redirectToUrl(null);
    }
	
	/**
     * 查看
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") String id,Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasViewPermission();
        }

        setCommonData(model);
        
        model.addAttribute("m", sysRoleService.findOne(id).getData());
        //展示授权信息
        this.permissions(id, model);
        model.addAttribute(Constants.OP_NAME, "查看");
        return viewName("editForm");
    }
    
    /**
     * 修改
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id}/update", method = RequestMethod.GET)
    public String showUpdateForm(@PathVariable("id") String id, Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "修改");
        
        SysRole m=sysRoleService.findOne(id).getData();
        model.addAttribute("m", m);
        //展示授权信息
        this.permissions(id, model);
        // 所有权限
     	model.addAttribute("permissions", sysPermissionService.findAll().getData());

        return viewName("editForm");
    }
    
    
    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(
            Model model, @ModelAttribute("m") SysRole m, BindingResult result,
            @RequestParam("resourceId") String[] resourceIds,
            @RequestParam("permissionIds") String[][] permissionIds,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        if (hasError(m, result)) {
            return showUpdateForm(m.getId(), model);
        }
        BaseResult baseResult=sysRoleService.updateRoleAndRoleResourcePermissions(m,
        		this.consivitionRoleResourcePermission(resourceIds, permissionIds));
        if(!baseResult.isSuccess()){
        	 redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	 redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改成功");
        }       
        return redirectToUrl(backURL);
    }
    
    /**
     * 删除
     * @param id
     * @param model
     * @return
     */
    @RequestMapping(value = "{id}/delete", method = RequestMethod.GET)
    public String showDeleteForm(@PathVariable("id") String id, Model model) {

        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "删除");
        model.addAttribute("m", sysRoleService.findOne(id).getData());
        //展示授权信息
        this.permissions(id, model);
        return viewName("editForm");
    }
    
    @RequestMapping(value = "{id}/delete", method = RequestMethod.POST)
    public String delete(
            @PathVariable("id") String id,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        BaseResult result= sysRoleService.deleteById(id);
        if(!result.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, result.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }        
        return redirectToUrl(backURL);
    }
    
    /**
     * 批量删除
     * @param ids
     * @param backURL
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteInBatch(
            @RequestParam(value = "ids", required = false) String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {

        if (permissionList != null) {
            this.permissionList.assertHasDeletePermission();
        }

        BaseResult baseResult= sysRoleService.batchDeleteByIds(ids);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE,baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }
        return redirectToUrl(backURL);
    }
    
    
    /**
     * 可用，不可用
     * @param request
     * @param ids
     * @param newStatus
     * @param redirectAttributes
     * @return
     */
    @RequestMapping(value = "changeStatus/{newStatus}", method = {RequestMethod.GET, RequestMethod.POST})
    public String changeStatus(
    		  HttpServletRequest request,
    		  @RequestParam(value = "ids", required = false) String[] ids,
    		  @PathVariable("newStatus") Boolean newStatus,
    		  RedirectAttributes redirectAttributes){
    	
    	if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }
    	
    	List<SysRole> roles=Lists.newArrayList();
    	for(String id:ids){
    		SysRole role=new SysRole();
    		role.setId(id);
    		role.setIsShow(newStatus);
    		roles.add(role);
    	}    	
    	BaseResult result=sysRoleService.batchUpdate(roles);
    	if(!result.isSuccess()){
    		redirectAttributes.addFlashAttribute(Constants.MESSAGE, result.getMessage());
    	}
    	
    	return redirectToUrl((String)request.getAttribute(Constants.BACK_URL));
    }	
	
	
	/**
	 * 展示角色下的菜单、权限
	 * @param role
	 * @return
	 */
	@RequestMapping("{roleId}/permissions")
    public String permissions(@PathVariable("roleId") String roleId,Model model) {
		
		this.permissionList.assertHasViewPermission();
		
		SysRoleResourcePermission sysRoleResourcePermission=new SysRoleResourcePermission();
		sysRoleResourcePermission.setRoleId(roleId);
		List<SysRoleResourcePermission> lists= sysRoleResourcePermissionService.find(sysRoleResourcePermission).getData();
		
		model.addAttribute("resouceIdAndPermissionIds", sysRoleResourcePermissionService.getResourceIdAndPermissionIdsMap(lists));	
        return viewName("permissionsTable");
    }
	

	private List<SysRoleResourcePermission> consivitionRoleResourcePermission(String[] resourceIds,String[][] permissionIds){
		List<SysRoleResourcePermission> list=Lists.newArrayList();
		
		if(ArrayUtils.isEmpty(resourceIds)){
			return list;
		}
		
		for(int i=0;i<resourceIds.length;i++){
			String[] permissionId=permissionIds[i];
			for(int j=0;j<permissionId.length;j++){
				SysRoleResourcePermission sysRoleResourcePermission=new SysRoleResourcePermission();
				sysRoleResourcePermission.setResourceId(resourceIds[i]);
				sysRoleResourcePermission.setPermissionId(permissionId[j]);
				list.add(sysRoleResourcePermission);
			}
		}		
		return list;
	}
}
