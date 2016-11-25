package com.anjz.core.enums;

/**
 * 图标类型
 * @author ding.shuai
 * @date 2016年8月29日下午4:30:14
 */
public enum IconType {
	css_class("css类图标"), upload_file("文件图标"), css_sprite("css精灵图标");

	private final String info;

	private IconType(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
}
