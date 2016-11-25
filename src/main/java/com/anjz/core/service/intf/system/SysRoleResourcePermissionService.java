package com.anjz.core.service.intf.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.result.ListResult;

/**
 * @author ding.shuai
 * @date 2016年8月25日上午9:32:34
 */
public interface SysRoleResourcePermissionService extends BaseService<SysRoleResourcePermission, String> {

	/**
	 * 通过角色Ids查询SysRoleResourcePermission集合
	 * @param roleIds
	 * @return
	 */
	public ListResult<SysRoleResourcePermission> findRoleResourcePermissionsByRoleIds(Set<String> roleIds);
	
	/**
	 * 获取资源ID，权限ID集合的map
	 * @param lists
	 * @return
	 */
	public Map<String, Set<String>> getResourceIdAndPermissionIdsMap(List<SysRoleResourcePermission> lists);

}
