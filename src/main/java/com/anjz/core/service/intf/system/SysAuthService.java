package com.anjz.core.service.intf.system;

import java.util.Set;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.SysAuth;
import com.anjz.core.model.SysUser;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午4:43:01
 */
public interface SysAuthService extends BaseService<SysAuth, String>{
	
	
	/**
	 * 通过用户获取用户对应的角色结合
	 * @param user
	 * @return 返回角色标识集合
	 */
	public PlainResult<Set<String>> findStringRoles(SysUser user);
	
	
	/**
	 * 根据角色获取 权限字符串 如sys:admin
	 * @param user
	 * @return
	 */
	public PlainResult<Set<String>> findStringPermissions(SysUser user);
	
	
	/**
	 * 添加用户授权
	 * @param auth
	 * @param userIds
	 * @return
	 */
	public BaseResult addUserAuth(SysAuth auth,String[] userIds);
	
	
	/**
	 * 添加组授权
	 * @param auth
	 * @param groupIds
	 * @return
	 */
	public BaseResult addGroupAuth(SysAuth auth,String[] groupIds);
	
	/**
	 * 添加组织机构和工作职务授权
	 * @param auth
	 * @param organizationIds
	 * @param jobs
	 * @return
	 */
	public BaseResult addOrganizationJobAuth(SysAuth auth,String[] organizationIds,String[][] jobIds); 

}
