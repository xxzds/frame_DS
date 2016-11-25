package com.anjz.core.model;

import com.anjz.base.entity.BaseEntity;
import com.anjz.core.enums.UserStatus;

public class SysUserStatusHistory extends BaseEntity {

	private static final long serialVersionUID = -1L;
	
	/**
	 * 被操作的用户ID
	 */
	private java.lang.String userId;
	/**
	 * 更改后的状态
	 */
	private UserStatus status;
	/**
	 * 操作原因
	 */
	private java.lang.String reason;
	/**
	 * 操作人ID
	 */
	private java.lang.String opUserId;
	/**
	 * 操作日期
	 */
	private java.util.Date opDate;
	
	public void setUserId(java.lang.String userId) {
		this.userId = userId;
	}

	public java.lang.String getUserId() {
		return this.userId;
	}
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public UserStatus getStatus() {
		return this.status;
	}
	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}

	public java.lang.String getReason() {
		return this.reason;
	}
	public void setOpUserId(java.lang.String opUserId) {
		this.opUserId = opUserId;
	}

	public java.lang.String getOpUserId() {
		return this.opUserId;
	}
	public void setOpDate(java.util.Date opDate) {
		this.opDate = opDate;
	}

	public java.util.Date getOpDate() {
		return this.opDate;
	}


}