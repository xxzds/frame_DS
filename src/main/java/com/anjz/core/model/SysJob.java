package com.anjz.core.model;
 
import org.apache.commons.lang.StringUtils;

import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.Treeable;


public class SysJob extends BaseEntity implements Treeable {

	private static final long serialVersionUID = -1L;
	

	/**
	 * 职位名称
	 */
	private java.lang.String name;

	/**
	 * 父节点ID
	 */
	private java.lang.String parentId;

	/**
	 * 父节点的路径集合，如0/1/
	 */
	private java.lang.String parentIds;

	/**
	 * 图标
	 */
	private java.lang.String icon;

	/**
	 * 权重
	 */
	private java.lang.Integer weight;

	/**
	 * 是否显示
	 */
	private java.lang.Boolean isShow;
	
	/**
	 * 是否有叶子节点(冗余)
	 */
	private boolean hasChildren;
	
	
	public void setIsShow(java.lang.Boolean isShow) {
		this.isShow = isShow;
	}

	public java.lang.Boolean getIsShow() {
		return this.isShow;
	}
	
	/**名称**/
	@Override
	public void setName(String name) {
		this.name=name;		
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	/**显示的图标 大小为16×16**/
	@Override
	public void setIcon(java.lang.String icon) {
		this.icon = icon;
	}

	@Override
	public java.lang.String getIcon() {
		return this.icon;
	}
	
	@Override
	public String getIconByDefault() {
		if (!StringUtils.isEmpty(icon)) {
			return icon;
		}
		if (isRoot()) {
			return getRootDefaultIcon();
		}
		if (isLeaf()) {
			return getLeafDefaultIcon();
		}
		return getBranchDefaultIcon();
	}
	
	/**父路径**/
	@Override
	public void setParentId(java.lang.String parentId) {
		this.parentId = parentId;
	}
	
	@Override
	public java.lang.String getParentId() {
		return this.parentId;
	}
	
	/**所有父路径**/
	@Override
	public void setParentIds(java.lang.String parentIds) {
		this.parentIds = parentIds;
	}
	
	@Override
	public java.lang.String getParentIds() {
		return this.parentIds;
	}
	
	
	/**获取 parentIds 之间的分隔符**/
	@Override
	public String getSeparator() {
		return "/";
	}

	/**把自己构造出新的父节点路径**/
	@Override
	public String makeSelfAsNewParentIds() {
		 return getParentIds() + getId() + getSeparator();
	}
	
	/**权重 用于排序 越小越排在前边**/
	@Override
	public void setWeight(java.lang.Integer weight) {
		this.weight = weight;
	}
	
	@Override
	public java.lang.Integer getWeight() {
		return this.weight;
	}

	/**是否是根节点**/
	@Override
	public boolean isRoot() {
		if (getParentId() != null && "0".equals(getParentId())) {
            return true;
        }
        return false;
	}

	/**是否是叶子节点**/
	@Override
	public boolean isLeaf() {
		if (isRoot()) {
            return false;
        }
        if (isHasChildren()) {
            return false;
        }

        return true;
	}

	/**是否有孩子节点**/
	@Override
	public boolean isHasChildren() {
		return this.hasChildren;
	}

	@Override
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}

	/**根节点默认图标 如果没有默认 空即可 大小为16×16**/
	@Override
	public String getRootDefaultIcon() {
		 return "ztree_root_open";
	}

	/**树枝节点默认图标 如果没有默认 空即可 大小为16×16**/
	@Override
	public String getBranchDefaultIcon() {
		 return "ztree_branch";
	}

	/**树叶节点默认图标 如果没有默认 空即可 大小为16×16**/
	@Override
	public String getLeafDefaultIcon() {
		 return "ztree_leaf";
	}


}