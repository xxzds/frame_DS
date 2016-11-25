package com.anjz.core.model;
 
import java.io.Serializable;

import com.anjz.base.entity.BaseEntity;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午4:44:52
 */
public class SysUserOrganizationJob extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 用户ID
	 */
	private java.lang.String userId;

	/**
	 * 组织机构ID
	 */
	private java.lang.String organizationId;

	/**
	 * 职位ID
	 */
	private java.lang.String jobId;
	
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
	
	public void setJobId(java.lang.String jobId) {
		this.jobId = jobId;
	}

	public java.lang.String getJobId() {
		return this.jobId;
	}


}