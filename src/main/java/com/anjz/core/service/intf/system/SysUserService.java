package com.anjz.core.service.intf.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.service.BaseService;
import com.anjz.core.enums.UserStatus;
import com.anjz.core.model.SysUser;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.result.BaseResult;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年8月24日上午9:40:39
 */
public interface SysUserService extends BaseService<SysUser, String>{
	
	/**
	 * 通过用户名查询实体
	 * @param userName
	 * @return
	 */
	public PlainResult<SysUser> findByUsername(String userName);
	
	
	/**
	 * 通过邮箱查询实体
	 * @param userEmail
	 * @return
	 */
	public PlainResult<SysUser> findByUserEmail(String userEmail);
	
	
	/**
	 * 通过手机号查询实体
	 * @param userPhone
	 * @return
	 */
	public PlainResult<SysUser> findByUserPhone(String userPhone);
	
	/**
	 * 通过条件查询实体，并分页
	 * @param organizationId 组织id
	 * @param jobId  职务id
	 * @param searchable
	 * @return
	 */
    public Page<SysUser> findSysUserWithPage(String organizationId,String jobId,Searchable searchable);
	
	
	/**
	 * 添加用户数据，以及用户的组织机构和职位
	 * @param user
	 * @param organizationJobs
	 * @return
	 */
	public BaseResult saveUserAndOrganazionAndJob(SysUser user,List<SysUserOrganizationJob> organizationJobs);
	
	/**
	 * 更新用户数据，以及用户的组织机构和职位
	 * @param user
	 * @param organizationJobs
	 * @return
	 */
	public BaseResult updateUserAndOrganazionAndJob(SysUser user,List<SysUserOrganizationJob> organizationJobs);

	/**
	 * 删除用户
	 * @param userId
	 * @return
	 */
	public BaseResult deleteUser(String userId);
	
	
	/**
	 * 批量更新用户数据
	 * @param users
	 * @return
	 */
	public BaseResult batchUpdate(List<SysUser> users);
	
	
	/**
	 * 修改用户密码
	 * @param userIds
	 * @param newPassword
	 * @return
	 */
	public BaseResult changePassword(String[] userIds,String newPassword);
	
	
	/**
	 * 更改用户状态
	 * @param opUser  操作人
	 * @param userIds  被操作的用户ID
	 * @param newStatus 新状态
	 * @param reason 操作原因
	 * @return
	 */
	public BaseResult changeStatus(SysUser opUser, String[] userIds,UserStatus newStatus,String reason);
	
	/**
	 * 通过用户名查询用户名和id存入map
	 * [{"label":用户名,"value":用户id},{}]
	 * @param searchable
	 * @param usernme
	 * @return
	 */
	public Set<Map<String, Object>> findIdAndNames(Searchable searchable, String usernme);

}
