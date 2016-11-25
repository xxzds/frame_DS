package com.anjz.core.model;
 
import java.util.Set;

import com.anjz.base.entity.BaseEntity;
import com.anjz.core.enums.AuthType;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年9月8日上午10:26:54
 */
public class SysAuth  extends BaseEntity {

	private static final long serialVersionUID = -1L;

	/**
	 * 组织机构ID
	 */
	private java.lang.String organizationId;

	/**
	 * 职位ID
	 */
	private java.lang.String jobId;

	/**
	 * 用户ID
	 */
	private java.lang.String userId;

	/**
	 * 组ID
	 */
	private java.lang.String groupId;

	/**
	 * 角色ID集合
	 */
	private java.lang.String roleIds;

	/**
	 * 类型
	 */
	private AuthType type;
	
	public void setOrganizationId(java.lang.String organizationId) {
		this.organizationId = organizationId;
	}

	public java.lang.String getOrganizationId() {
		return this.organizationId;
	}
	
	public void setJobId(java.lang.String jobId) {
		this.jobId = jobId;
	}

	public java.lang.String getJobId() {
		return this.jobId;
	}
	
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	public java.lang.String getUserId() {
		return this.userId;
	}
	
	public void setGroupId(java.lang.String groupId) {
		this.groupId = groupId;
	}

	public java.lang.String getGroupId() {
		return this.groupId;
	}
	
	public void setRoleIds(java.lang.String roleIds) {
		this.roleIds = roleIds;
	}

	public java.lang.String getRoleIds() {
		return this.roleIds;
	}
	
	public void setType(AuthType type) {
		this.type = type;
	}

	public AuthType getType() {
		return this.type;
	}
	
	//获取角色集合数组
	public String[] getRoleIdsArray(){
		if(this.roleIds==null){
			return new String[]{};
		}
		return this.roleIds.split(",");
	}
	public Set<String> getRoleIdsSet(){
		return Sets.newHashSet(getRoleIdsArray());
	}
 

}