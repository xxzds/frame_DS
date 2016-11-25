package com.anjz.core.model;
 
import java.io.Serializable;

import com.anjz.base.entity.BaseEntity;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午4:36:18
 */
public class SysRoleResourcePermission extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 角色ID
	 */
	private java.lang.String roleId;

	/**
	 * 资源ID
	 */
	private java.lang.String resourceId;

	/**
	 * 权限ID
	 */
	private java.lang.String permissionId;
	
	
	public void setRoleId(java.lang.String roleId) {
		this.roleId = roleId;
	}

	public java.lang.String getRoleId() {
		return this.roleId;
	}
	
	public void setResourceId(java.lang.String resourceId) {
		this.resourceId = resourceId;
	}

	public java.lang.String getResourceId() {
		return this.resourceId;
	}
	
	public void setPermissionId(java.lang.String permissionId) {
		this.permissionId = permissionId;
	}

	public java.lang.String getPermissionId() {
		return this.permissionId;
	}
}