package com.anjz.core.service.intf.system;

import java.util.List;
import java.util.Set;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.SysRole;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午11:48:05
 */
public interface SysRoleService  extends BaseService<SysRole, String>{

	/**
     * 获取可用的角色列表
     *
     * @param roleIds
     * @return
     */
    public Set<SysRole> findShowRoles(Set<String> roleIds);
    
    
    /**
     * 保存角色和角色资源权限集合
     * @param role
     * @param roleResourcePermissions
     * @return
     */
    public BaseResult saveRoleAndRoleResourcePermissions(SysRole role,List<SysRoleResourcePermission> roleResourcePermissions);
    
    
    /**
     * 修改角色和角色资源权限集合
     * @param role
     * @param roleResourcePermissions
     * @return
     */
    public BaseResult updateRoleAndRoleResourcePermissions(SysRole role,List<SysRoleResourcePermission> roleResourcePermissions);
    
    /**
     * 批量更新
     * @param roles
     * @return
     */
    public BaseResult batchUpdate(List<SysRole> roles);
}
