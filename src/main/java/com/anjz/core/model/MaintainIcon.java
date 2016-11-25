package com.anjz.core.model;

import com.anjz.base.entity.BaseEntity;
import com.anjz.core.enums.IconType;


public class MaintainIcon extends BaseEntity {

	private static final long serialVersionUID = -1L;
	
	/**
	 * 标识符
	 */
	private java.lang.String identity;
	/**
	 * 标识符(css_class有数据)
	 */
	private java.lang.String cssClass;
	/**
	 * upload_file路径
	 */
	private java.lang.String imgSrc;
	/**
	 * 宽
	 */
	private java.lang.Integer width;
	/**
	 * 高
	 */
	private java.lang.Integer height;
	/**
	 * css_sprite路径
	 */
	private java.lang.String spriteSrc;
	/**
	 * 左侧偏移量
	 */
	private java.lang.Integer offsetLeft;
	/**
	 * 顶部偏移量
	 */
	private java.lang.Integer offsetTop;
	/**
	 * style
	 */
	private java.lang.String style;
	/**
	 * 类型css_class("css类图标"), upload_file("文件图标"), css_sprite("css精灵图标")
	 */
	private IconType type;
	/**
	 * 描述
	 */
	private java.lang.String description;
	
	public void setIdentity(java.lang.String identity) {
		this.identity = identity;
	}

	public java.lang.String getIdentity() {
		return this.identity;
	}
	public void setCssClass(java.lang.String cssClass) {
		this.cssClass = cssClass;
	}

	public java.lang.String getCssClass() {
		return this.cssClass;
	}
	public void setImgSrc(java.lang.String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public java.lang.String getImgSrc() {
		return this.imgSrc;
	}
	public void setWidth(java.lang.Integer width) {
		this.width = width;
	}

	public java.lang.Integer getWidth() {
		return this.width;
	}
	public void setHeight(java.lang.Integer height) {
		this.height = height;
	}

	public java.lang.Integer getHeight() {
		return this.height;
	}
	public void setSpriteSrc(java.lang.String spriteSrc) {
		this.spriteSrc = spriteSrc;
	}

	public java.lang.String getSpriteSrc() {
		return this.spriteSrc;
	}
	

	public java.lang.Integer getOffsetLeft() {
		return offsetLeft;
	}

	public void setOffsetLeft(java.lang.Integer offsetLeft) {
		this.offsetLeft = offsetLeft;
	}

	public java.lang.Integer getOffsetTop() {
		return offsetTop;
	}

	public void setOffsetTop(java.lang.Integer offsetTop) {
		this.offsetTop = offsetTop;
	}

	public void setStyle(java.lang.String style) {
		this.style = style;
	}

	public java.lang.String getStyle() {
		return this.style;
	}
	public void setType(IconType type) {
		this.type = type;
	}

	public IconType getType() {
		return this.type;
	}
	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.String getDescription() {
		return this.description;
	}


}