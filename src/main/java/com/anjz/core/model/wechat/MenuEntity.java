package com.anjz.core.model.wechat;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 自定义菜单实体
 * @author ding.shuai
 * @date 2016年9月25日下午3:03:18
 */
public class MenuEntity {
	private List<Button> button=Lists.newArrayList();

	public List<Button> getButton() {
		return button;
	}

	public void setButton(List<Button> button) {
		this.button = button;
	}
	
	
	/**
	 * 内部类
	 * @author ding.shuai
	 * @date 2016年9月25日下午3:10:09
	 */
	public static class Button{
		private String type;
		
		private String name;
		
		private String key;
		
		private String url;
		
		private List<Button> sub_button;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public List<Button> getSub_button() {
			return sub_button;
		}

		public void setSub_button(List<Button> sub_button) {
			this.sub_button = sub_button;
		}
	}

}