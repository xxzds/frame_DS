package com.anjz.core.model.wechat;

/**
 * 查询菜单，返回的实体
 * @author ding.shuai
 * @date 2016年9月27日下午5:54:59
 */
public class QueryMenuEntity {

	/**
	 * 默认菜单
	 */
	private MenuEntity menu;
	
	/**
	 * 个性化菜单
	 */
	private MenuEntity conditionalmenu;

	public MenuEntity getMenu() {
		return menu;
	}

	public void setMenu(MenuEntity menu) {
		this.menu = menu;
	}

	public MenuEntity getConditionalmenu() {
		return conditionalmenu;
	}

	public void setConditionalmenu(MenuEntity conditionalmenu) {
		this.conditionalmenu = conditionalmenu;
	}
}
