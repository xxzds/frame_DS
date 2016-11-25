package com.anjz.core.service.impl.system;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.model.SysRole;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.core.service.intf.system.SysRoleResourcePermissionService;
import com.anjz.core.service.intf.system.SysRoleService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * 授权权限给角色，增删改，需要清除授权缓存
 * @author ding.shuai
 * @date 2016年8月26日上午11:48:35
 */
@Service
public class SysRoleServiceImpl extends BaseServiceImpl<SysRole, String> implements SysRoleService{

	@Resource
	private SysRoleResourcePermissionService sysRoleResourcePermissionService;
	
	@Resource
	private CacheManager cacheManager;
	
	/**
     * 获取可用的角色列表
     *
     * @param roleIds
     * @return
     */
    public Set<SysRole> findShowRoles(Set<String> roleIds){       
        if(roleIds.isEmpty()){
        	return Sets.newHashSet();
        }
               
        Searchable searchable=new SearchRequest()
        		.addSearchFilter("id", SearchOperator.in, roleIds)
        		.addSearchFilter("is_show", SearchOperator.eq, true);
               
        return Sets.newHashSet(this.findAllWithNoPageNoSort(searchable));
    }
    
    /**
     * 保存角色和角色资源权限集合
     * @param role
     * @param roleResourcePermissions
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult saveRoleAndRoleResourcePermissions(SysRole role,List<SysRoleResourcePermission> roleResourcePermissions){
    	BaseResult result=new BaseResult();
    	
    	if(role==null){
    		throw new BusinessException("保存角色实体异常");
    	}
    	
    	BaseResult saveRoleResult = this.saveSelective(role);
    	if(!saveRoleResult.isSuccess()){
    		throw  new BusinessException("保存角色失败");
    	}
    	
    	
    	if(!roleResourcePermissions.isEmpty()){
    		String roleId=role.getId();
        	for(SysRoleResourcePermission sysRoleResourcePermission:roleResourcePermissions){
        		sysRoleResourcePermission.setRoleId(roleId);        		
        		BaseResult  saveRRPResult= sysRoleResourcePermissionService.saveSelective(sysRoleResourcePermission);
        		if(!saveRRPResult.isSuccess()){
        			throw new BusinessException("保存角色的授权信息失败");
        		}
        	}
    	}
    	//清除缓存
		clearAuthCache();
    	return result;
    }
    
    /**
     * 修改角色和角色资源权限集合
     * @param role
     * @param roleResourcePermissions
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult updateRoleAndRoleResourcePermissions(SysRole role,List<SysRoleResourcePermission> roleResourcePermissions){
    	BaseResult result=new BaseResult();
    	
    	if(role==null){
    		throw new BusinessException("修改角色实体异常");
    	}
    	
    	//1.删除角色资源权限集合
    	SysRoleResourcePermission m=new SysRoleResourcePermission();
    	m.setRoleId(role.getId());
    	List<SysRoleResourcePermission> list= sysRoleResourcePermissionService.find(m).getData();
    	
    	if(!list.isEmpty()){
    		Set<String> rrpIds=Sets.newHashSet(Lists.transform(list, new Function<SysRoleResourcePermission, String>() {
        		@Override
        		public String apply(SysRoleResourcePermission input) {
        			return input.getId();
        		}
    		}));
    		
    		BaseResult deleteResult= sysRoleResourcePermissionService.batchDeleteByIds(rrpIds.toArray(new String[]{}));
    		if(!deleteResult.isSuccess()){
    			throw new BusinessException("清除角色的资源信息失败");
    		}
    	}
    	
    	//2.更新角色，添加资源信息
    	BaseResult updateRoleResult= this.updateSelectiveById(role);
    	if(!updateRoleResult.isSuccess()){
    		throw new BusinessException("修改角色失败");
    	}
    	
    	if(!roleResourcePermissions.isEmpty()){
    		String roleId=role.getId();
        	for(SysRoleResourcePermission sysRoleResourcePermission:roleResourcePermissions){
        		sysRoleResourcePermission.setRoleId(roleId);        		
        		BaseResult  saveRRPResult= sysRoleResourcePermissionService.saveSelective(sysRoleResourcePermission);
        		if(!saveRRPResult.isSuccess()){
        			throw new BusinessException("保存角色的授权信息失败");
        		}
        	}
    	}
    	//清除缓存
		clearAuthCache();
    	return result;
    }
    
    
    
    /**
     * 重写删除方法
     * 删除角色，以及角色的关联数据
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult deleteById(String id) {
    	
    	//删除授权信息
    	SysRoleResourcePermission m=new SysRoleResourcePermission();
    	m.setRoleId(id);
    	List<SysRoleResourcePermission> list= sysRoleResourcePermissionService.find(m).getData();
    	
    	if(!list.isEmpty()){
    		Set<String> rrpIds=Sets.newHashSet(Lists.transform(list, new Function<SysRoleResourcePermission, String>() {
        		@Override
        		public String apply(SysRoleResourcePermission input) {
        			return input.getId();
        		}
    		}));
    		
    		BaseResult deleteResult= sysRoleResourcePermissionService.batchDeleteByIds(rrpIds.toArray(new String[]{}));
    		if(!deleteResult.isSuccess()){
    			throw new BusinessException("清除角色的资源信息失败");
    		}
    	}
    	
    	BaseResult result= super.deleteById(id);   	
    	if(result.isSuccess()){
    		//清除缓存
			clearAuthCache();
    	}
    	return result;   	
    }
    
    
    
    /**
     * 重写批量删除方法
     * 删除角色，以及角色的关联数据
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult batchDeleteByIds(String[] ids) {
    	
    	if(ids==null || ids.length==0){
    		return new BaseResult();
    	}
    	
    	//删除授权信息
    	Searchable searchable=new SearchRequest()
    			.addSearchFilter("role_id", SearchOperator.in, ids);
    	List<SysRoleResourcePermission> list= sysRoleResourcePermissionService.findAllWithNoPageNoSort(searchable);
    	
    	if(!list.isEmpty()){
    		Set<String> rrpIds=Sets.newHashSet(Lists.transform(list, new Function<SysRoleResourcePermission, String>() {
        		@Override
        		public String apply(SysRoleResourcePermission input) {
        			return input.getId();
        		}
    		}));
    		
    		BaseResult deleteResult= sysRoleResourcePermissionService.batchDeleteByIds(rrpIds.toArray(new String[]{}));
    		if(!deleteResult.isSuccess()){
    			throw new BusinessException("清除角色的资源信息失败");
    		}
    	}

    	BaseResult result= super.batchDeleteByIds(ids);
    	if(result.isSuccess()){
    		//清除缓存
			clearAuthCache();
    	}
    	return result;
    }
    
    /**
     * 批量更新
     * @param roles
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult batchUpdate(List<SysRole> roles){
    	if(roles.isEmpty()){
    		return new BaseResult();
    	}
    	   	
    	for(SysRole role:roles){
    		BaseResult result= this.updateSelectiveById(role);
    		if(!result.isSuccess()){
    			throw new BusinessException("批量更新失败");
    		}
    	}
    	//清除缓存
		clearAuthCache();
    	return new BaseResult();
    }
    
    /**
	 * 清除授权的缓存
	 */
	public void clearAuthCache(){
		Cache cache= cacheManager.getCache("authorizationCache");
        cache.clear();
	}
}
