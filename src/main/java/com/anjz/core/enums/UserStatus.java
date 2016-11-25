package com.anjz.core.enums;

/**
 * 用户状态
 * @author ding.shuai
 * @date 2016年8月22日上午11:30:32
 */
public enum UserStatus {
	normal(0, "正常状态"), blocked(1, "封禁状态");

	public final int state;
	public final String info;

	private UserStatus(int state, String info) {
		this.state = state;
		this.info = info;
	}

	public int getState() {
		return state;
	}

	public String getInfo() {
		return info;
	}

	public static UserStatus valueOf(Integer state) {
		for (UserStatus userStatus : values()) {
			if (state != null && userStatus.state == state) {
				return userStatus;
			}
		}
		return null;
	}

}
