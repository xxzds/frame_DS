package com.anjz.core.model;
 
import java.io.Serializable;

import com.anjz.base.entity.BaseEntity;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午4:36:06
 */
public class SysPermission extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1L;
	
	/**
	 * 权限名称
	 */
	private java.lang.String name;

	/**
	 * 权限标识
	 */
	private java.lang.String permission;

	/**
	 * 详细描述
	 */
	private java.lang.String description;

	/**
	 * 是否显示
	 */
	private java.lang.Boolean isShow;
	
	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getName() {
		return this.name;
	}
	
	public void setPermission(java.lang.String permission) {
		this.permission = permission;
	}

	public java.lang.String getPermission() {
		return this.permission;
	}
	
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getDescription() {
		return this.description;
	}
	
	public void setIsShow(java.lang.Boolean isShow) {
		this.isShow = isShow;
	}

	public java.lang.Boolean getIsShow() {
		return this.isShow;
	}


}