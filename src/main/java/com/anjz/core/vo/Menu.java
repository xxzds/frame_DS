package com.anjz.core.vo;

import java.io.Serializable;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 界面展示的菜单对象
 * 
 * @author ding.shuai
 * @date 2016年8月26日下午5:21:57
 */
public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	private String icon;
	private String url;

	private List<Menu> children;

	public Menu(String id, String name, String icon, String url) {
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<Menu> getChildren() {
		if (children == null) {
			children = Lists.newArrayList();
		}
		return children;
	}

	public void setChildren(List<Menu> children) {
		this.children = children;
	}

	/**
	 * @return
	 */
	public boolean isHasChildren() {
		return !getChildren().isEmpty();
	}

	@Override
	public String toString() {
		return "Menu{" + "id=" + id + ", name='" + name + '\'' + ", icon='" + icon + '\'' + ", url='" + url + '\''
				+ ", children=" + children + '}';
	}
}
