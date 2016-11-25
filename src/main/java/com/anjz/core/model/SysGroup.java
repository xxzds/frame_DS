package com.anjz.core.model;
 
import java.io.Serializable;

import com.anjz.base.entity.BaseEntity;
import com.anjz.core.enums.GroupType;


public class SysGroup extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -1L;

	/**
	 * 组名
	 */
	private java.lang.String groupName;

	/**
	 * 类型(包括用户分组和组织分组)
	 */
	private GroupType type;

	/**
	 * 是否显示
	 */
	private java.lang.Boolean isShow;
	
	
	public void setGroupName(java.lang.String groupName) {
		this.groupName = groupName;
	}

	public java.lang.String getGroupName() {
		return this.groupName;
	}
	
	public void setType(GroupType type) {
		this.type = type;
	}

	public GroupType getType() {
		return this.type;
	}
	
	public void setIsShow(java.lang.Boolean isShow) {
		this.isShow = isShow;
	}

	public java.lang.Boolean getIsShow() {
		return this.isShow;
	}


}