package com.anjz.core.service.impl.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.dao.SysRoleResourcePermissionDao;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.core.service.intf.system.SysRoleResourcePermissionService;
import com.anjz.result.ListResult;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年8月25日上午9:32:41
 */
@Service
public class SysRoleResourcePermissionServiceImpl extends BaseServiceImpl<SysRoleResourcePermission, String> implements SysRoleResourcePermissionService{
	
	@Resource
	private SysRoleResourcePermissionDao sysRoleResourcePermissionDao;

	/**
	 * 通过角色Ids查询SysRoleResourcePermission集合
	 * @param roleIds
	 * @return
	 */
	@Override
	public ListResult<SysRoleResourcePermission> findRoleResourcePermissionsByRoleIds(Set<String> roleIds) {
		ListResult<SysRoleResourcePermission> result=new ListResult<SysRoleResourcePermission>();
		
		if(roleIds.isEmpty()){
			return result;
		}
		
		Searchable searchable=new SearchRequest()
				.addSearchFilter("role_id", SearchOperator.in, roleIds);		
		List<SysRoleResourcePermission> list=  this.findAllWithNoPageNoSort(searchable);
		result.setData(list);
		
		return result;
	}
	
	/**
	 * 获取资源ID，权限ID集合的map
	 * @param lists
	 * @return
	 */
	@Override
	public Map<String, Set<String>> getResourceIdAndPermissionIdsMap(List<SysRoleResourcePermission> lists){
		if(lists.isEmpty()){
			return null;
		}

		Map<String, Set<String>> resouceIdAndPermissionIds=Maps.newHashMap();
		
		for(SysRoleResourcePermission m:lists){
			String resourceId=m.getResourceId();
			
			Set<String> permissions= resouceIdAndPermissionIds.get(resourceId);
			if(permissions==null){
				permissions=Sets.newHashSet();
				resouceIdAndPermissionIds.put(resourceId, permissions);
			}
			permissions.add(m.getPermissionId());
		}
		return resouceIdAndPermissionIds;
	}


}
