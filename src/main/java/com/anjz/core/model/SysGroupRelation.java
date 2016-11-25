package com.anjz.core.model;
 
import java.io.Serializable;

import com.anjz.base.entity.BaseEntity;


/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:43:02
 */
public class SysGroupRelation extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1L;
	

	/**
	 * 分组ID
	 */
	private java.lang.String groupId;

	/**
	 * 用户ID
	 */
	private java.lang.String userId;

	/**
	 * 组织机构ID
	 */
	private java.lang.String organizationId;
	
	public void setGroupId(java.lang.String groupId) {
		this.groupId = groupId;
	}

	public java.lang.String getGroupId() {
		return this.groupId;
	}
	
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	public java.lang.String getUserId() {
		return this.userId;
	}
	
	public void setOrganizationId(java.lang.String organizationId) {
		this.organizationId = organizationId;
	}

	public java.lang.String getOrganizationId() {
		return this.organizationId;
	}


}