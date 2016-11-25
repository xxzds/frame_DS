package com.anjz.base.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.Treeable;
import com.anjz.base.entity.search.Searchable;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午5:17:43
 */
public interface BaseTreeableService<M extends BaseEntity & Treeable, ID extends Serializable>
		extends BaseService<M, ID> {

	/**
	 * 获取下一个权重值
	 * 
	 * @param id 父节点的id
	 * @return
	 */
	public int nextWeight(String id);

	/**
	 * 删除自己和孩子节点
	 * @param m
	 */
	public void deleteSelfAndChild(M m);

	public void deleteSelfAndChild(List<M> mList);

	/**
     * 添加子节点
     * @param parent
     * @param child
     */
	public void appendChild(M parent, M child);

	/**
	 * 移动节点 根节点不能移动
	 *
	 * @param source
	 *            源节点
	 * @param target
	 *            目标节点
	 * @param moveType
	 *            位置
	 */
	public void move(M source, M target, String moveType);

	

	/**
	 *  查看与name模糊匹配的名称
	 * @param searchable
	 * @param name
	 * @param excludeId
	 * @return
	 */
	public Set<String> findNames(Searchable searchable, String name, ID excludeId);

	public List<M> findAllByName(Searchable searchable, M excludeM);

	/**
	 * 查询子子孙孙
	 *
	 * @return
	 */
	public List<M> findChildren(List<M> parents, Searchable searchable);

	/**
	 * 查找根和一级节点
	 *
	 * @param searchable
	 * @return
	 */
	public List<M> findRootAndChild(Searchable searchable);

	
	/**
	 * 查询所有祖先的Id集合
	 * 
	 * @param currentIds
	 * @return
	 */
	public Set<ID> findAncestorIds(Iterable<ID> currentIds);

	public Set<ID> findAncestorIds(ID currentId);
	
	/**
	 * 查询所有祖先集合
	 *
	 * @param parentIds
	 * @return
	 */
	public List<M> findAncestor(String parentIds);
	
	/**
	 * 添加移除某个节点和它的子节点的过滤条件
	 * @param searchable
	 * @param excludeM
	 */
	public void addExcludeSearchFilter(Searchable searchable, M excludeM);

}
