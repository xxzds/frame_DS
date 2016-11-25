package com.anjz.core.enums;

/**
 * 数据字典类型
 * @author ding.shuai
 * @date 2016年9月14日下午12:01:57
 */
public enum DictionaryType {
	template_type("模板类型","MBLX");
	
	private final String name;
	private final String code;

	private DictionaryType(String name,String code) {
		this.name=name();
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}
}
