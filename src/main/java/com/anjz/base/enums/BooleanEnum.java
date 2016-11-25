package com.anjz.base.enums;

/**
 * @author ding.shuai
 * @date 2016年8月27日下午4:03:17
 */
public enum BooleanEnum {
	TRUE(Boolean.TRUE, "是"), FALSE(Boolean.FALSE, "否");

	private final Boolean value;
	private final String info;

	private BooleanEnum(Boolean value, String info) {
		this.value = value;
		this.info = info;
	}

	public String getInfo() {
		return info;
	}

	public Boolean getValue() {
		return value;
	}
	
	public static BooleanEnum valueOf(Boolean value) {
		for (BooleanEnum booleanEnum : values()) {
			if (value != null && booleanEnum.value == value) {
				return booleanEnum;
			}
		}
		return null;
	}
}
