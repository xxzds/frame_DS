package com.anjz.core.model;

import com.anjz.base.entity.BaseEntity;

public class MaintainTemplate extends BaseEntity {

	private static final long serialVersionUID = -1L;
	
	/**
	 * 模板类型，关联数据字典
	 */
	private java.lang.String type;
	/**
	 * 模板内容
	 */
	private java.lang.String template;
	/**
	 * 是否已逻辑删除
	 */
	private java.lang.Boolean deleted;
	
	public void setType(java.lang.String type) {
		this.type = type;
	}

	public java.lang.String getType() {
		return this.type;
	}
	public void setTemplate(java.lang.String template) {
		this.template = template;
	}

	public java.lang.String getTemplate() {
		return this.template;
	}
	public void setDeleted(java.lang.Boolean deleted) {
		this.deleted = deleted;
	}

	public java.lang.Boolean getDeleted() {
		return this.deleted;
	}


}