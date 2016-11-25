package com.anjz.core.service.impl.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.dao.SysAuthDao;
import com.anjz.core.model.SysAuth;
import com.anjz.core.model.SysGroup;
import com.anjz.core.model.SysPermission;
import com.anjz.core.model.SysResource;
import com.anjz.core.model.SysRole;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.core.model.SysUser;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.core.service.intf.system.SysAuthService;
import com.anjz.core.service.intf.system.SysGroupService;
import com.anjz.core.service.intf.system.SysJobService;
import com.anjz.core.service.intf.system.SysOrganizationService;
import com.anjz.core.service.intf.system.SysPermissionService;
import com.anjz.core.service.intf.system.SysResourceService;
import com.anjz.core.service.intf.system.SysRoleResourcePermissionService;
import com.anjz.core.service.intf.system.SysRoleService;
import com.anjz.core.service.intf.system.SysUserOrganizationJobService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

/**
 * 授权角色给实体，增删改，需要清除授权缓存
 * @author ding.shuai
 * @date 2016年8月25日下午4:42:41
 */
@Service
public class SysAuthServiceImpl extends BaseServiceImpl<SysAuth, String> implements SysAuthService {

	@Resource
	private SysRoleResourcePermissionService sysRoleResourcePermissionService;

	@Resource
	private SysResourceService sysResourceService;

	@Resource
	private SysPermissionService sysPermissionService;

	@Resource
	private SysUserOrganizationJobService sysUserOrganizationJobService;

	@Resource
	private SysOrganizationService sysOrganizationService;

	@Resource
	private SysJobService sysJobService;
	
	@Resource
	private SysGroupService sysGroupService;
	
	@Resource
	private SysRoleService sysRoleService;
	
	@Resource
	private SysAuthDao sysAuthDao;
	
	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private CacheManager cacheManager;

	/**
	 * 通过用户获取用户对应的角色结合
	 * @param user
	 * @return 返回角色标识集合
	 */
	@Override
	public PlainResult<Set<String>> findStringRoles(SysUser user) {
		PlainResult<Set<String>> result = new PlainResult<Set<String>>();
		Set<SysRole> roles = this.findRoles(user);
		Set<String> data = Sets.newHashSet(Collections2.transform(roles, new Function<SysRole, String>() {
			@Override
			public String apply(SysRole input) {
				return input.getRole();
			}
		}));
		result.setData(data);
		return result;
	}

	/**
	 * 根据角色获取 权限字符串 如sys:admin
	 * @param user
	 * @return
	 */
	@Override
	public PlainResult<Set<String>> findStringPermissions(SysUser user) {
		PlainResult<Set<String>> result = new PlainResult<Set<String>>();
		Set<String> permissions = Sets.newHashSet();

		Set<SysRole> roles = this.findRoles(user);
		Set<String> roleIds = Sets.newHashSet(Collections2.transform(roles, new Function<SysRole, String>() {
			@Override
			public String apply(SysRole input) {
				return input.getId();
			}
		}));

		List<SysRoleResourcePermission> roleResourcePermissions = sysRoleResourcePermissionService.findRoleResourcePermissionsByRoleIds(roleIds).getData();		
		Map<String, Set<String>> resouceIdAndPermissionIds= sysRoleResourcePermissionService.getResourceIdAndPermissionIdsMap(roleResourcePermissions);
		
		for(Map.Entry<String, Set<String>> entry:resouceIdAndPermissionIds.entrySet()){
			SysResource sysResource = sysResourceService.findOne(entry.getKey()).getData();
			
			String actualResourceIdentity = sysResourceService.findActualResourceIdentity(sysResource).getData();
			
			// 不可用 即没查到 或者标识字符串不存在
			if (sysResource == null || StringUtils.isEmpty(actualResourceIdentity) || Boolean.FALSE.equals(sysResource.getIsShow())) {
				continue;
			}
			
			for(String permissionId:entry.getValue()){
				SysPermission permission = sysPermissionService.findOne(permissionId).getData();

				// 不可用
				if (permission == null || Boolean.FALSE.equals(permission.getIsShow())) {
					continue;
				}
				permissions.add(actualResourceIdentity + ":" + permission.getPermission());
			}
		}
		result.setData(permissions);
		return result;
	}

	/**
	 * 获取用户的角色集合
	 * 
	 * @param user
	 * @return
	 */
	private Set<SysRole> findRoles(SysUser user) {
		if (user == null) {
			return Sets.newHashSet();
		}

		String userId = user.getId();

		Set<String[]> organizationJobIds = Sets.newHashSet();
		Set<String> organizationIds = Sets.newHashSet();
		Set<String> jobIds = Sets.newHashSet();

		List<SysUserOrganizationJob> organizationJobs = sysUserOrganizationJobService.findOrganizationJobByUserId(userId).getData();
		for (SysUserOrganizationJob userOrganizationJob : organizationJobs) {
			String organizationId = userOrganizationJob.getOrganizationId();
			String jobId = userOrganizationJob.getJobId();

			if (StringUtils.isNotEmpty(organizationId) && StringUtils.isNotEmpty(jobId)) {
				organizationJobIds.add(new String[] { organizationId, jobId });
			}
			organizationIds.add(organizationId);
			jobIds.add(jobId);
		}

		// 找组织机构的祖先
		organizationIds.addAll(sysOrganizationService.findAncestorIds(organizationIds));
		// 找工作职务的祖先
		jobIds.addAll(sysJobService.findAncestorIds(jobIds));

		// 过滤组织机构 仅获取目前可用的组织机构数据
		sysOrganizationService.filterForCanShow(organizationIds, organizationJobIds);

		// 过滤工作职务 仅获取目前可用的工作职务数据
		sysJobService.filterForCanShow(jobIds, organizationJobIds);

		// 默认分组 + 根据用户编号 和 组织编号 找 分组
		Set<String> groupIds = sysGroupService.findShowGroupIds(userId, organizationIds);
		
		//获取用户角色Id集合
		 Set<String> roleIds = this.findRoleIds(userId, groupIds, organizationIds, jobIds, organizationJobIds);

		Set<SysRole> roles = sysRoleService.findShowRoles(roleIds);
		return roles;
	}
	
	/**
     * 根据用户信息获取角色
     * 1.1、用户  根据用户绝对匹配
     * 1.2、组织机构 根据组织机构绝对匹配 此处需要注意 祖先需要自己获取
     * 1.3、工作职务 根据工作职务绝对匹配 此处需要注意 祖先需要自己获取
     * 1.4、组织机构和工作职务  根据组织机构和工作职务绝对匹配 此处不匹配祖先
     * 1.5、组  根据组绝对匹配
     *
     * @param userId             必须有
     * @param groupIds           可选
     * @param organizationIds    可选
     * @param jobIds             可选
     * @param organizationJobIds 可选
     * @return
     */
    private Set<String> findRoleIds(String userId, Set<String> groupIds, Set<String> organizationIds, Set<String> jobIds, Set<String[]> organizationJobIds){
    	List<String> roleIdSets= sysAuthDao.findRoleIds(userId, groupIds, organizationIds, jobIds, organizationJobIds);
    	
    	Set<String> roleIds = Sets.newHashSet();
    	 for (String role_ids : roleIdSets) {    		 
    		 String[] roleIdArray= role_ids.split(",");
    		 for(String roleId:roleIdArray){
    			 roleIds.add(roleId);
    		 }
         }   	
    	return roleIds;
    }
    
    /**
	 * 添加用户授权
	 * @param auth
	 * @param userIds
	 * @return
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult addUserAuth(SysAuth auth,String[] userIds){
		BaseResult result=new BaseResult();
		
		if (ArrayUtils.isEmpty(userIds)) {
            return result;
        }
		
		for(String userId:userIds){
			SysUser user= sysUserService.findOne(userId).getData();
			if(user==null){
				continue;
			}
			
			SysAuth param=new SysAuth();
			param.setUserId(userId);
			param.setType(auth.getType());
			List<SysAuth> list= this.find(param).getData();
			if(list!=null && list.size()>0){
				param=list.get(0);
				
				//合并角色集合
				Set<String> roleIdsSet1= param.getRoleIdsSet();				
				Set<String> roleIdsSet2=auth.getRoleIdsSet();				
				SetView<String> roleIdsSet=  Sets.union(roleIdsSet1, roleIdsSet2);				
				param.setRoleIds(StringUtils.join(roleIdsSet, ","));
				
				BaseResult updateResult= this.updateSelectiveById(param);
				if(!updateResult.isSuccess()){
					throw new BusinessException("批量添加用户授权失败");
				}
			    continue;
			}
			auth.setUserId(userId);
			BaseResult saveResult= this.saveSelective(auth);
			if(!saveResult.isSuccess()){
				throw new BusinessException("批量添加用户授权失败");
			}
		}
		//清除缓存
		clearAuthCache();
		return result;
	}
    
    /**
	 * 添加组授权
	 * @param auth
	 * @param groupIds
	 * @return
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult addGroupAuth(SysAuth auth,String[] groupIds){
    	BaseResult result=new BaseResult();
		
		if (ArrayUtils.isEmpty(groupIds)) {
            return result;
        }
		
		for(String groupId:groupIds){
			SysGroup group= sysGroupService.findOne(groupId).getData();
			if(group==null){
				continue;
			}
			
			SysAuth param=new SysAuth();
			param.setGroupId(groupId);
			param.setType(auth.getType());
			List<SysAuth> list= this.find(param).getData();
			if(list!=null && list.size()>0){
				param=list.get(0);
				
				Set<String> roleIdsSet1= param.getRoleIdsSet();				
				Set<String> roleIdsSet2=auth.getRoleIdsSet();				
				SetView<String> roleIdsSet=  Sets.union(roleIdsSet1, roleIdsSet2);
				
				param.setRoleIds(StringUtils.join(roleIdsSet, ","));
				
				BaseResult updateResult= this.updateSelectiveById(param);
				if(!updateResult.isSuccess()){
					throw new BusinessException("批量添加组授权失败");
				}
			    continue;
			}
			auth.setGroupId(groupId);
			BaseResult saveResult= this.saveSelective(auth);
			if(!saveResult.isSuccess()){
				throw new BusinessException("批量添加组授权失败");
			}
		}
		//清除缓存
		clearAuthCache();
		return result;
	}
    
    /**
	 * 添加组织机构和工作职务授权
	 * @param auth
	 * @param organizationIds
	 * @param jobs
	 * @return
	 */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult addOrganizationJobAuth(SysAuth auth,String[] organizationIds,String[][] jobIds){
		BaseResult result=new BaseResult();
		if (ArrayUtils.isEmpty(organizationIds)) {
            return result;
        }
		
		for(int i=0;i<organizationIds.length;i++){
			String organizationId=organizationIds[i];
			for(int j=0;j<jobIds.length;j++){
				String jobId=jobIds[i][j];
				BaseResult baseResult= this.addOrganizationJobAuth(auth, organizationId, jobId);
				if(!baseResult.isSuccess()){
					throw new BusinessException("添加组织机构和工作职务授权失败");
				}
			}
		}
		//清除缓存
		clearAuthCache();
		return result;
	}
	
	private BaseResult addOrganizationJobAuth(SysAuth auth,String organizationId,String jobId){
		
		if(StringUtils.isEmpty(organizationId) || StringUtils.isEmpty(jobId)){
			throw new BusinessException("组织机构或工作职位不能为空");
		}
			
		SysAuth param=new SysAuth();
		param.setType(auth.getType());
		param.setOrganizationId(organizationId);
		param.setJobId(jobId);
		List<SysAuth> list= this.find(param).getData();
		if(!list.isEmpty()){
			param=list.get(0);
			
			Set<String> roleIdsSet1= param.getRoleIdsSet();				
			Set<String> roleIdsSet2=auth.getRoleIdsSet();				
			SetView<String> roleIdsSet=  Sets.union(roleIdsSet1, roleIdsSet2);				
			
			param.setRoleIds(StringUtils.join(roleIdsSet, ","));
			
			BaseResult updateResult= this.updateSelectiveById(param);
			if(!updateResult.isSuccess()){
				throw new BusinessException("添加组织机构和工作职务授权失败");
			}
			
		}else{
			auth.setOrganizationId(organizationId);
			auth.setJobId(jobId);
			BaseResult saveResult= this.saveSelective(auth);
			if(!saveResult.isSuccess()){
				throw new BusinessException("添加组织机构和工作职务授权失败");
			}
		}	
		//清除缓存
		clearAuthCache();
		return new BaseResult();
	}
	
	
	
	@Override
	public BaseResult updateSelectiveById(SysAuth m) {		
		BaseResult result= super.updateSelectiveById(m);
		if(result.isSuccess()){
			//清除缓存
			clearAuthCache();
		}
		return result;
	}
	
	@Override
	public BaseResult deleteById(String id) {		
		BaseResult result= super.deleteById(id);
		if(result.isSuccess()){
			//清除缓存
			clearAuthCache();
		}
		return result;
	}
	
	@Override
	public BaseResult batchDeleteByIds(String[] ids) {
		BaseResult result= super.batchDeleteByIds(ids);
		if(result.isSuccess()){
			//清除缓存
			clearAuthCache();
		}
		return result;
	}
	
	
	
	/**
	 * 清除授权的缓存
	 */
	public void clearAuthCache(){
		Cache cache= cacheManager.getCache("authorizationCache");
        cache.clear();
	}
	
}
