package com.anjz.base.entity;

import java.io.Serializable;

/**
 * <p>
 * 实体实现该接口表示想要实现树结构
 * 
 * @author ding.shuai
 * @date 2016年8月22日上午10:59:13
 */
public interface Treeable  extends Serializable{

	public void setName(String name);

	public String getName();

	/**
	 * 显示的图标 大小为16×16
	 *
	 * @return
	 */
	public String getIcon();

	public void setIcon(String icon);
	
	/**
	 * 如果没有，会给个默认图标
	 * @return
	 */
	public String getIconByDefault();

	/**
	 * 父路径(id暂且固定为String类型)
	 *
	 * @return
	 */
	public String getParentId();

	public void setParentId(String parentId);

	/**
	 * 所有父路径 如1,2,3,
	 *
	 * @return
	 */
	public String getParentIds();

	public void setParentIds(String parentIds);

	/**
	 * 获取 parentIds 之间的分隔符
	 *
	 * @return
	 */
	public String getSeparator();

	/**
	 * 把自己构造出新的父节点路径
	 *
	 * @return
	 */
	public String makeSelfAsNewParentIds();

	/**
	 * 权重 用于排序 越小越排在前边
	 *
	 * @return
	 */
	public Integer getWeight();

	public void setWeight(Integer weight);

	/**
	 * 是否是根节点
	 *
	 * @return
	 */
	public boolean isRoot();

	/**
	 * 是否是叶子节点
	 *
	 * @return
	 */
	public boolean isLeaf();

	/**
	 * 是否有孩子节点
	 *
	 * @return
	 */
	public boolean isHasChildren();
	
	public void setHasChildren(boolean hasChildren);

	/**
	 * 根节点默认图标 如果没有默认 空即可 大小为16×16
	 */
	public String getRootDefaultIcon();

	/**
	 * 树枝节点默认图标 如果没有默认 空即可 大小为16×16
	 */
	public String getBranchDefaultIcon();

	/**
	 * 树叶节点默认图标 如果没有默认 空即可 大小为16×16
	 */
	public String getLeafDefaultIcon();
	
	
	/**
	 * 是否显示
	 * @param isShow
	 */
	public void setIsShow(java.lang.Boolean isShow);

	public java.lang.Boolean getIsShow();

}
