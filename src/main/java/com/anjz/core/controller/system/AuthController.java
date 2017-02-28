package com.anjz.core.controller.system;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.anjz.base.bind.annotation.SearchableDefaults;
import com.anjz.base.controller.BaseCRUDController;
import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.PageImpl;
import com.anjz.core.enums.AuthType;
import com.anjz.core.model.SysAuth;
import com.anjz.core.model.SysGroup;
import com.anjz.core.model.SysJob;
import com.anjz.core.model.SysOrganization;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.SysAuthService;
import com.anjz.core.service.intf.system.SysGroupService;
import com.anjz.core.service.intf.system.SysJobService;
import com.anjz.core.service.intf.system.SysOrganizationService;
import com.anjz.core.service.intf.system.SysRoleService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.result.BaseResult;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 授权角色给实体
 * 
 * @author ding.shuai
 * @date 2016年9月8日上午10:25:42
 */
@Controller
@RequestMapping("/system/permission/auth")
public class AuthController extends BaseCRUDController<SysAuth, String> {

	@Resource
	private SysAuthService sysAuthService;
	
	@Resource
	private SysRoleService sysRoleService;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private SysGroupService sysGroupService;
	
	@Resource
	private SysOrganizationService sysOrganizationService;
	
	@Resource
	private SysJobService sysJobService;

	public AuthController() {
		setResourceIdentity("system:auth");
		setListAlsoSetCommonData(true);
	}

	@Override
	protected void setCommonData(Model model) {
		model.addAttribute("types", AuthType.values());
		// 所有角色
		model.addAttribute("roles", sysRoleService.findAll().getData());
		
	}

	@SearchableDefaults(value = "type-eq=user")
	@Override
	public String list(Searchable searchable, Model model) {
		String typeName = String.valueOf(searchable.getValue("type-eq"));
		AuthType type= AuthType.valueOf(typeName);
		model.addAttribute("type", type);
		
		//添加不同的查询条件
		switch (type) {
		case user: {
			if(searchable.containsSearchKey("username-like")){
				String username= searchable.getValue("username-like");
				
				Searchable searchable1=new SearchRequest()
						.addSearchFilter("user_name", SearchOperator.like, username);
				List<SysUser> list= sysUserService.findAllWithNoPageNoSort(searchable1);
				
				if(list.isEmpty()){
					model.addAttribute("page", new PageImpl<SysAuth>(new ArrayList<SysAuth>()));
					return viewName("list");
				}
				Set<String> userIds=Sets.newHashSet(Lists.transform(list, new Function<SysUser, String>() {
					@Override
					public String apply(SysUser input) {
						return input.getId();
					}
				}));
				searchable.addSearchFilter("user_id", SearchOperator.in, userIds);
				searchable.removeSearchFilter("username-like");
			}
			
		}
		break;
		case user_group:
		case organization_group: {
			if(searchable.containsSearchKey("groupname-like")){
				String groupname= searchable.getValue("groupname-like");
				
				Searchable searchable1=new SearchRequest()
						.addSearchFilter("group_name", SearchOperator.like, groupname)
						.addSearchFilter("type", SearchOperator.eq, type == AuthType.user_group ? "user":"organization" );
				List<SysGroup> list= sysGroupService.findAllWithNoPageNoSort(searchable1);
				
				if(list.isEmpty()){
					model.addAttribute("page", new PageImpl<SysAuth>(new ArrayList<SysAuth>()));
					return viewName("list");
				}
				Set<String> groupIds=Sets.newHashSet(Lists.transform(list, new Function<SysGroup, String>() {
					@Override
					public String apply(SysGroup input) {
						return input.getId();
					}
				}));
				searchable.addSearchFilter("group_id", SearchOperator.in, groupIds);
				searchable.removeSearchFilter("groupname-like");
			}

		}
		break;
		case organization_job: {
				if(searchable.containsSearchKey("organizationname-like")){
					String organizatonname=searchable.getValue("organizationname-like");
					
					Searchable searchable1=new SearchRequest()
							.addSearchFilter("name", SearchOperator.like, organizatonname);
					List<SysOrganization> list= sysOrganizationService.findAllWithNoPageNoSort(searchable1);
					if(list.isEmpty()){
						model.addAttribute("page", new PageImpl<SysAuth>(new ArrayList<SysAuth>()));
						return viewName("list");
					}
					Set<String> organizationIds=Sets.newHashSet(Lists.transform(list, new Function<SysOrganization, String>() {
						@Override
						public String apply(SysOrganization input) {
							return input.getId();
						}
					}));
					searchable.addSearchFilter("organization_id", SearchOperator.in, organizationIds);
					searchable.removeSearchFilter("organizationname-like");
				}
				if(searchable.containsSearchKey("jobname-like")){
					String jobname=searchable.getValue("jobname-like");
					
					Searchable searchable1=new SearchRequest()
							.addSearchFilter("name", SearchOperator.like, jobname);
					List<SysJob> list= sysJobService.findAllWithNoPageNoSort(searchable1);
					if(list.isEmpty()){
						model.addAttribute("page", new PageImpl<SysAuth>(new ArrayList<SysAuth>()));
						return viewName("list");
					}
					Set<String> jobIds=Sets.newHashSet(Lists.transform(list, new Function<SysJob, String>() {
						@Override
						public String apply(SysJob input) {
							return input.getId();
						}
					}));
					searchable.addSearchFilter("job_id", SearchOperator.in, jobIds);
					searchable.removeSearchFilter("jobname-like");
				}
		}
		break;

		default:
			break;
		}
		
		return super.list(searchable, model);
	}
	
	
	@Override
	@SearchableDefaults(value = "type-eq=user",merge=true)
	public String listTable(Searchable searchable, Model model) {
		return super.listTable(searchable, model);
	}
	

	/**
	 * 添加
	 */
	@RequestMapping(value = "{type}/create", method = RequestMethod.GET)
	public String showCreateFormWithType(Model model, @PathVariable("type") AuthType type) {
		SysAuth auth = new SysAuth();
		auth.setType(type);
		model.addAttribute("m", auth);
		return super.showCreateForm(model);
	}

	@RequestMapping(value = "{type}/create", method = RequestMethod.POST)
	public String createWithType(HttpServletRequest request,Model model,
			 @RequestParam(value = "userIds", required = false) String[] userIds,
			 @RequestParam(value = "groupIds", required = false) String[] groupIds,
			 @RequestParam(value = "organizationIds", required = false) String[] organizationIds,
	         @RequestParam(value = "jobIds", required = false) String[][] jobIds,
	         @ModelAttribute("m") SysAuth m, BindingResult result,
	         RedirectAttributes redirectAttributes) {
		
		if(this.permissionList!=null){
			this.permissionList.assertHasCreatePermission();
		}
		
		if (hasError(m, result)) {
            return showCreateForm(model);
        }
		
		BaseResult baseResult=null;
		if(m.getType()==AuthType.user){
			baseResult=sysAuthService.addUserAuth(m,userIds);
		}else if(m.getType()==AuthType.user_group || m.getType()==AuthType.organization_group){
			baseResult=sysAuthService.addGroupAuth(m, groupIds);
		}else if(m.getType()==AuthType.organization_job){
			baseResult=sysAuthService.addOrganizationJobAuth(m, organizationIds, jobIds);
		}
		
		if(!baseResult.isSuccess()){
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
		}else{
			redirectAttributes.addFlashAttribute(Constants.MESSAGE, "新增成功");
		}

		return redirectToUrl((String) request.getAttribute(Constants.BACK_URL));
	}
}
