package com.anjz.core.model;

import com.anjz.base.entity.BaseEntity;

public class ShowcaseEditor extends BaseEntity {

	private static final long serialVersionUID = -1L;
	
	/**
	 * 标题
	 */
	private java.lang.String title;
	/**
	 * 内容
	 */
	private java.lang.String content;
	
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	public java.lang.String getTitle() {
		return this.title;
	}
	public void setContent(java.lang.String content) {
		this.content = content;
	}

	public java.lang.String getContent() {
		return this.content;
	}


}