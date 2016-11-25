package com.anjz.core.model;

import javax.validation.constraints.NotNull;

import com.anjz.base.entity.BaseEntity;

/**
 * @author ding.shuai
 * @date 2016年9月21日上午10:05:43
 */
public class ShowcaseUpload extends BaseEntity {

	private static final long serialVersionUID = -1L;
	/**
	 * 文件名
	 */
	@NotNull(message = "{not.null}")
	private java.lang.String name;
	/**
	 * 文件路径
	 */
	private java.lang.String src;
	
	public void setName(java.lang.String name) {
		this.name = name;
	}

	public java.lang.String getName() {
		return this.name;
	}
	public void setSrc(java.lang.String src) {
		this.src = src;
	}

	public java.lang.String getSrc() {
		return this.src;
	}


}