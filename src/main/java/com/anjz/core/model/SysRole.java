package com.anjz.core.model;

import java.io.Serializable;

import com.anjz.base.entity.BaseEntity;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午11:47:45
 */
public class SysRole extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 角色名称
	 */
	private java.lang.String name;

	/**
	 * 角色标识
	 */
	private java.lang.String role;

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

	public void setRole(java.lang.String role) {
		this.role = role;
	}

	public java.lang.String getRole() {
		return this.role;
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