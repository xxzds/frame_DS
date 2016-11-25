package com.anjz.core.controller.system;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.CurrentUser;
import com.anjz.base.bind.annotation.PageableDefaults;
import com.anjz.base.controller.BaseController;
import com.anjz.base.controller.permission.PermissionList;
import com.anjz.base.entity.response.ValidateResponse;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.enums.BooleanEnum;
import com.anjz.core.enums.UserStatus;
import com.anjz.core.model.SysUser;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.core.service.intf.system.SysUserOrganizationJobService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.result.BaseResult;
import com.google.common.collect.Lists;

/**
 * 用户
 * 
 * @author ding.shuai
 * @date 2016年8月17日下午4:59:01
 */
@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController<SysUser, String>{
	protected PermissionList permissionList = null;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private SysUserOrganizationJobService sysUserOrganizationJobService;

	public UserController() {
		setResourceIdentity("system:user");
	}
	
	/**
	 * 权限前缀：如sys:user 则生成的新增权限为 sys:user:create
	 */
	public void setResourceIdentity(String resourceIdentity) {
		if (!StringUtils.isEmpty(resourceIdentity)) {
			permissionList = PermissionList.newPermissionList(resourceIdentity);
		}
	}

	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("statusList", UserStatus.values());
		model.addAttribute("booleanList", BooleanEnum.values());
			
		//设置默认值
		SysUser user=new SysUser();
		user.setStatus(UserStatus.normal);
		model.addAttribute("m",user);
	}
	
	/**
	 * 主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "main", method = RequestMethod.GET)
    public String main(Model model) {
        return viewName("main");
    }

	
    @RequestMapping(value = "tree", method = RequestMethod.GET)
    public String tree(Model model) {
        return viewName("tree");
    }
	
    /**
     * 列表
     * @param organizationId
     * @param jobId
     * @param searchable
     * @param model
     * @return
     */
	@RequestMapping(value = { "{organizationId}/{jobId}" }, method = RequestMethod.GET)
	@PageableDefaults(sort = "id=desc")
	public String list(
			@PathVariable("organizationId") String organizationId,
			@PathVariable("jobId") String jobId,
			Searchable searchable, Model model) {
		
		if (permissionList != null) {
			this.permissionList.assertHasViewPermission();
		}	

		model.addAttribute("page", sysUserService.findSysUserWithPage(organizationId, jobId, searchable));
		model.addAttribute("organizationId", organizationId);
		model.addAttribute("jobId", jobId);
		setCommonData(model);
		return viewName("list");
	}
	
	/**
     * 仅返回表格数据
     *
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(value = { "{organizationId}/{jobId}" },method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String listTable(
    		@PathVariable("organizationId") String organizationId,
			@PathVariable("jobId") String jobId,
    		Searchable searchable, Model model) {
        list(organizationId,jobId, searchable, model);
        return viewName("listTable");
    }
    
    
    /**
     * 选择用户
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(value ="selectUser" ,method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String selectUser(Searchable searchable, Model model) {        
    	model.addAttribute("page", sysUserService.findAll(searchable));
    	
        return viewName("selectUser");
    }
    
    @RequestMapping(value ="selectUserTable" ,method = RequestMethod.GET, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String selectUserTable(Searchable searchable, Model model) {        
    	model.addAttribute("page", sysUserService.findAll(searchable));
    	
        return viewName("selectUserTable");
    }
	
	
	@RequestMapping("{userId}/organizations")
    public String organizationJobs(@PathVariable String userId,Model model) {		
		List<SysUserOrganizationJob> userOrganizationJobs= sysUserOrganizationJobService.findOrganizationJobByUserId(userId).getData();
				
		model.addAttribute("organizationJobsMap", sysUserOrganizationJobService.getDisplayOrganizationJobs(userOrganizationJobs));
        return viewName("organizationsTable");
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
        model.addAttribute("m", sysUserService.findOne(id).getData());
        model.addAttribute(Constants.OP_NAME, "查看");
        
        //查询机构，职务数据
        organizationJobs(id,model);
        return viewName("editForm");
    }
	
	
	/**
	 * 添加用户
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
            model.addAttribute("m", newModel());
        }
        return viewName("editForm");
    }
		
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(HttpServletRequest request, Model model,  
			@Valid @ModelAttribute("m") SysUser m, BindingResult result,
			@RequestParam(value = "organizationId", required = false) String[] organizationIds,
	        @RequestParam(value = "jobId", required = false) String[][] jobIds,
            RedirectAttributes redirectAttributes){
		
		if (permissionList != null) {
            this.permissionList.assertHasCreatePermission();
        }

        if (hasError(m, result)) {
            return showCreateForm(model);
        }
        
        BaseResult baseResult=sysUserService.saveUserAndOrganazionAndJob(m, conversionUserOrganizationJob(organizationIds,jobIds));
        
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
        }      
        return redirectToUrl((String)request.getAttribute(Constants.BACK_URL));
	}	
	
	
	
	/**
	 * 修改
	 */	
	@RequestMapping(value = "{id}/update", method = RequestMethod.GET)
	public String showUpdateForm(@PathVariable("id") String id, Model model){
		
		if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "修改");
        
        SysUser user=sysUserService.findOne(id).getData();
        model.addAttribute("m", user);
        
        //查询机构，职务数据
        organizationJobs(id, model);
        
        return viewName("editForm");
				
	}
	
	@RequestMapping(value = "{id}/update", method = RequestMethod.POST)
	public String update(Model model,  
			@Valid @ModelAttribute("m") SysUser m, BindingResult result,
			@RequestParam(value = "organizationId", required = false) String[] organizationIds,
	        @RequestParam(value = "jobId", required = false) String[][] jobIds,
	        @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
		
		if (permissionList != null) {
            this.permissionList.assertHasUpdatePermission();
        }

        if (hasError(m, result)) {
            return showUpdateForm(m.getId(), model);
        }
        
        
        BaseResult baseResult=sysUserService.updateUserAndOrganazionAndJob(m, conversionUserOrganizationJob(organizationIds,jobIds));
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
        model.addAttribute("m", sysUserService.findOne(id).getData());
        
        //查询机构，职务数据
        organizationJobs(id, model);
        
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

        BaseResult result= sysUserService.deleteUser(id);
        if(!result.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, result.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
        }        
        return redirectToUrl(backURL);
    }
	
	
	/**
	 *批量删除(逻辑删除)
	 */
	@RequestMapping(value = "batch/delete", method = {RequestMethod.GET, RequestMethod.POST})
	public String deleteUserInBatch(
			@RequestParam(value = "ids", required = false) String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
			RedirectAttributes redirectAttributes){
		
		 if (permissionList != null) {
	            this.permissionList.assertHasDeletePermission();
	        }

		 	List<SysUser> users=Lists.newArrayList();
		 	for(String id:ids){
		 		SysUser user=new SysUser();
		 		user.setId(id);
		 		user.setDeleted(true);
		 		users.add(user);
		 	}
	        BaseResult baseResult= sysUserService.batchUpdate(users);
	        if(!baseResult.isSuccess()){
	        	redirectAttributes.addFlashAttribute(Constants.MESSAGE,baseResult.getMessage());
	        }else{
	        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "删除成功");
	        }
	        return redirectToUrl(backURL);
	}
	
	
	/**
	 * 修改密码
	 * @param request
	 * @param ids
	 * @param newPassword
	 * @param opUser
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "changePassword")
	public String changePassword(HttpServletRequest request, @RequestParam("ids") String[] ids,
			@RequestParam("newPassword") String newPassword, RedirectAttributes redirectAttributes) {

		if (permissionList != null) {
			this.permissionList.assertHasUpdatePermission();
		}
		
		BaseResult result= sysUserService.changePassword(ids, newPassword);
		if(result.isSuccess()){
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "改密成功！");
		}else{
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, result.getMessage());
		}
		
		return redirectToUrl((String) request.getAttribute(Constants.BACK_URL));
	}
	
	
	/**
	 * 封禁用户/解封用户
	 * @param request
	 * @param opUser
	 * @param ids
	 * @param newStatus
	 * @param reason
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "changeStatus/{newStatus}")
    public String changeStatus(
            HttpServletRequest request,
            @CurrentUser SysUser opUser,
            @RequestParam("ids") String[] ids,
            @PathVariable("newStatus") UserStatus newStatus,
            @RequestParam("reason") String reason,
            RedirectAttributes redirectAttributes) {

		BaseResult result= sysUserService.changeStatus(opUser, ids, newStatus, reason);
		if(!result.isSuccess()){
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, result.getMessage());
		}else{
			if (newStatus == UserStatus.normal) {
	            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "解封成功！");
	        } else if (newStatus == UserStatus.blocked) {
	            redirectAttributes.addFlashAttribute(Constants.MESSAGE, "封禁成功！");
	        }
		}
        return redirectToUrl((String) request.getAttribute(Constants.BACK_URL));
    }
	
	
	/**
	 * 还原删除的用户
	 * @param request
	 * @param ids
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "recycle")
    public String recycle(HttpServletRequest request, 
    		@RequestParam("ids") String[] ids, RedirectAttributes redirectAttributes) {
        		
		List<SysUser> users=Lists.newArrayList();
	 	for(String id:ids){
	 		SysUser user=new SysUser();
	 		user.setId(id);
	 		user.setDeleted(false);
	 		users.add(user);
	 	}
        BaseResult baseResult= sysUserService.batchUpdate(users);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "还原失败！");
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "还原成功！");
        }
        return redirectToUrl((String) request.getAttribute(Constants.BACK_URL));
    }
	
	
	/**
	 * 自动填充用户名
	 * @param searchable
	 * @param term
	 * @return
	 */
	@RequestMapping("ajax/autocomplete")
    @PageableDefaults(value = 30)
    @ResponseBody
    public Set<Map<String, Object>> autocomplete(Searchable searchable,
            @RequestParam("term") String term) {

        return sysUserService.findIdAndNames(searchable, term);
    }
	
	

	 /**
     * 验证返回格式
     * 单个：[fieldId, 1|0, msg]
     * 多个：[[fieldId, 1|0, msg],[fieldId, 1|0, msg]]
     *
     * @param fieldId
     * @param fieldValue
     * @return
     */
    @RequestMapping(value = "validate", method = RequestMethod.GET)
    @ResponseBody
    public Object validate(
            @RequestParam("fieldId") String fieldId, @RequestParam("fieldValue") String fieldValue,
            @RequestParam(value = "id", required = false) String id) {

        ValidateResponse response = ValidateResponse.newInstance();


        if ("userName".equals(fieldId)) {
            SysUser user = sysUserService.findByUsername(fieldValue).getData();
            if (user == null || (user.getId().equals(id) && user.getUserName().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "用户名已被其他人使用");
            }
        }

        if ("userEmail".equals(fieldId)) {
        	SysUser user = sysUserService.findByUserEmail(fieldValue).getData();
            if (user == null || (user.getId().equals(id) && user.getUserEmail().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "邮箱已被其他人使用");
            }
        }

        if ("userPhone".equals(fieldId)) {
        	SysUser user = sysUserService.findByUserPhone(fieldValue).getData();
            if (user == null || (user.getId().equals(id) && user.getUserPhone().equals(fieldValue))) {
                //如果msg 不为空 将弹出提示框
                response.validateSuccess(fieldId, "");
            } else {
                response.validateFail(fieldId, "手机号已被其他人使用");
            }
        }

        return response.result();
    }
    
    
    private List<SysUserOrganizationJob> conversionUserOrganizationJob(String[] organizationIds,String[][] jobIds){
		List<SysUserOrganizationJob> list = Lists.newArrayList();

		if (ArrayUtils.isEmpty(organizationIds)) {
			return list;
		}

		for (int i = 0; i < organizationIds.length; i++) {
			String[] jobId = jobIds[i];
			for (int j = 0; j < jobId.length; j++) {
				SysUserOrganizationJob sysUserOrganizationJob = new SysUserOrganizationJob();
				sysUserOrganizationJob.setOrganizationId(organizationIds[i]);
				sysUserOrganizationJob.setJobId(jobIds[i][j]);		
				list.add(sysUserOrganizationJob);
			}
		}
		return list;
	}	
     
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
    	//String -> Date
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
