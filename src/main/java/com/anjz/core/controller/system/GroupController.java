package com.anjz.core.controller.system;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.anjz.base.controller.BaseCRUDController;
import com.anjz.base.controller.permission.PermissionList;
import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.PageImpl;
import com.anjz.base.enums.BooleanEnum;
import com.anjz.core.enums.GroupType;
import com.anjz.core.model.SysGroup;
import com.anjz.core.model.SysGroupRelation;
import com.anjz.core.model.SysOrganization;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.SysGroupRelationService;
import com.anjz.core.service.intf.system.SysGroupService;
import com.anjz.core.service.intf.system.SysOrganizationService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.result.BaseResult;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 分组列表
 * @author ding.shuai
 * @date 2016年9月6日上午10:57:08
 */
@Controller
@RequestMapping("/system/group")
public class GroupController extends BaseCRUDController<SysGroup, String>{
	
	@Resource
	private SysGroupService sysGroupService;
	
	@Resource
	private SysGroupRelationService sysGroupRelationService;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private SysOrganizationService sysOrganizationService;

	public GroupController() {
		setResourceIdentity("system:group");
		setListAlsoSetCommonData(true);
	}
	
	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("types", GroupType.values());
		model.addAttribute("booleanList", BooleanEnum.values());
	}
	
	
	/**
	 * 展示用户组列表，组织机构组列表
	 */
	@RequestMapping(value = "{type}/list", method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String list(@PathVariable("type") GroupType type, Searchable searchable, Model model,HttpServletRequest request) {
        searchable.addSearchFilter("type", SearchOperator.eq, type);
        
        System.out.println(request.getParameter("type"));
        return list(searchable, model);
    }
	
	/**
	 * 增加
	 */	
	@RequestMapping(value = "{type}/create", method = RequestMethod.GET)
    public String showCreateFormWithType(@PathVariable("type") GroupType type, Model model) {
        if (!model.containsAttribute("m")) {
            SysGroup group = new SysGroup();
            group.setType(type);            
            //默认值
            group.setIsShow(true);
            model.addAttribute("m", group);
        }
        return super.showCreateForm(model);
    }
	
	
	@RequestMapping(value = "{type}/create", method = RequestMethod.POST)
    public String create(
    		HttpServletRequest request,
            Model model, @ModelAttribute("m") SysGroup m, BindingResult result,
            RedirectAttributes redirectAttributes) {

        return super.create(request,model, m, result, redirectAttributes);
    }
	
	/**
	 * 选中有效、选中无效
	 */
	@RequestMapping(value = "/changeStatus/{newStatus}")
    public String changeShowStatus(HttpServletRequest request,
            @PathVariable("newStatus") Boolean newStatus,
            @RequestParam("ids") String[] ids) {

		if(this.permissionList!=null){
			this.permissionList.assertHasUpdatePermission();
		}
        
		List<SysGroup> groups=Lists.newArrayList();
		for(String id:ids){
			SysGroup group=new SysGroup();
			group.setId(id);
			group.setIsShow(newStatus);
			groups.add(group);
		}
		
		sysGroupService.batchUpdate(groups);
        return redirectToUrl((String)request.getAttribute(Constants.BACK_URL));
    }

		
	/************************************************分组的关联操作****************************************************/
	@RequestMapping(value = "/{groupId}/listRelation", method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String listGroupRelation(@PathVariable("groupId") String groupId, Searchable searchable, Model model) {   	

    	if(permissionList!=null){
    		this.permissionList.assertHasViewPermission();
    	}
        
    	SysGroup group = sysGroupService.findOne(groupId).getData();
    	model.addAttribute("group", group);

    	searchable.addSearchFilter("group_id", SearchOperator.eq, groupId);
    	Page<SysGroupRelation> page= sysGroupRelationService.findAll(searchable);
    	model.addAttribute("page", page);

        return viewName("relation/relationList");
    }
	
	@RequestMapping(value = "/{groupId}/listRelation", headers = "table=true", method = RequestMethod.GET)
    @PageableDefaults(sort = "id=desc")
    public String listGroupRelationTable(@PathVariable("groupId") String groupId, Searchable searchable, Model model) {
		
		//用户名称查询
		if(searchable.containsSearchKey("user_name|like")){
			Searchable searchable1=new SearchRequest().addSearchParam("user_name|like", searchable.getValue("user_name|like"));
			
			List<SysUser> users= sysUserService.findAllWithNoPageNoSort(searchable1);
			if(users.isEmpty()){
				model.addAttribute("page", new PageImpl<SysGroupRelation>(new ArrayList<SysGroupRelation>()));
				return viewName("relation/relationListTable");
			}
			searchable.removeSearchFilter("user_name", SearchOperator.like);
			searchable.addSearchFilter("user_id", SearchOperator.in,
					Sets.newHashSet(Lists.transform(users, new Function<SysUser, String>() {
						@Override
						public String apply(SysUser input) {
							return input.getId();
						}})));
		}
		
		//机构名称查询
		if(searchable.containsSearchKey("name|like")){
			Searchable searchable2=new SearchRequest().addSearchParam("name|like", searchable.getValue("name|like"));
			
			List<SysOrganization> organizations= sysOrganizationService.findAllWithNoPageNoSort(searchable2);
			if(organizations.isEmpty()){
				model.addAttribute("page", new PageImpl<SysGroupRelation>(new ArrayList<SysGroupRelation>()));
				return viewName("relation/relationListTable");
			}
			searchable.removeSearchFilter("name", SearchOperator.like);
			searchable.addSearchFilter("organization_id", SearchOperator.in, 
					Sets.newHashSet(Lists.transform(organizations, new Function<SysOrganization, String>() {
				@Override
				public String apply(SysOrganization input) {
					return input.getId();
				}})));
		}
        this.listGroupRelation(groupId, searchable, model);
        return viewName("relation/relationListTable");
    }
	
	
	/**
	 * 批量增加
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "{groupId}/batch/append", method = RequestMethod.GET)
    public String showBatchAppendGroupRelationForm(@PathVariable("groupId") String groupId,Model model) {
		
		this.permissionList.assertHasAnyPermission(new String[]{PermissionList.CREATE_PERMISSION, PermissionList.UPDATE_PERMISSION});

    	SysGroup group= sysGroupService.findOne(groupId).getData();
    	model.addAttribute("group", group);
        if (group.getType() == GroupType.user) {
            return viewName("relation/appendUserGroupRelation");
        }

        if (group.getType() == GroupType.organization) {
            return viewName("relation/appendOrganizationGroupRelation");
        }

        throw new RuntimeException("group type error");
    }
	
	@RequestMapping(value = "{groupId}/batch/append", method = RequestMethod.POST)
    public String batchAppendGroupRelation(
            @PathVariable("groupId") String groupId,
            @RequestParam("ids") String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes) {
		
		this.permissionList.assertHasAnyPermission(new String[]{PermissionList.CREATE_PERMISSION, PermissionList.UPDATE_PERMISSION});
		
		SysGroup group= sysGroupService.findOne(groupId).getData();
		
        BaseResult baseResult= sysGroupRelationService.appendRelation(group,ids);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "批量添加成功");
        }
        
        return redirectToUrl(backURL);
    }
	
	
	/**
	 * 批量删除
	 * @param groupId
	 * @param ids
	 * @param backURL
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "{groupId}/batch/delete", method = RequestMethod.GET)
	public String batchDeleteGroupRelation(
            @RequestParam("ids") String[] ids,
            @RequestParam(value = Constants.BACK_URL, required = false) String backURL,
            RedirectAttributes redirectAttributes){
		
		if(permissionList!=null){
    		this.permissionList.assertHasDeletePermission();
    	}
		
		BaseResult baseResult= sysGroupRelationService.batchDeleteByIds(ids);
		if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "批量删除成功");
        }		
		return redirectToUrl(backURL);
	}
	
	/**
     * 选择分组
     * @param searchable
     * @param model
     * @return
     */
    @RequestMapping(value ="selectGroup" ,method = {RequestMethod.GET,RequestMethod.POST}, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String selectUser(Searchable searchable, Model model) {        
    	model.addAttribute("page", sysGroupService.findAll(searchable));
    	
        return viewName("selectGroup");
    }
    
    @RequestMapping(value ="selectGroupTable" ,method = {RequestMethod.GET,RequestMethod.POST}, headers = "table=true")
    @PageableDefaults(sort = "id=desc")
    public String selectUserTable(Searchable searchable, Model model) {        
    	model.addAttribute("page", sysGroupService.findAll(searchable));
    	
        return viewName("selectGroupTable");
    }
}
