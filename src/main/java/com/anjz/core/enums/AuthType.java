package com.anjz.core.enums;

/**
 * 授权类型
 * @author ding.shuai
 * @date 2016年9月8日上午10:29:09
 */
public enum AuthType {
	user("用户"), user_group("用户组"), organization_job("组织机构和工作职务"), organization_group("组织机构组");

	private final String info;

	private AuthType(String info) {
		this.info = info;
	}

	public String getInfo() {
		return info;
	}
}
