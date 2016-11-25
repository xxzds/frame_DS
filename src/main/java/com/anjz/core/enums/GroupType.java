package com.anjz.core.enums;

/**
 * 用户组分类
 * @author ding.shuai
 * @date 2016年9月6日上午11:06:32
 */
public enum GroupType {
	user("用户组"), organization("组织机构组");

	private final String info;

	private GroupType(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

}
