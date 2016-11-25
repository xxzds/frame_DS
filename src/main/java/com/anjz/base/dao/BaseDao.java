package com.anjz.base.dao;

import java.util.List;

import com.anjz.base.entity.search.Searchable;

/**
 * Dao通用接口
 * @author ding.shuai
 * @date 2016年7月24日上午8:14:15
 */
public interface BaseDao<T,K> {
	
	/**
     * 获取单个实体
     * @param id
     * @return
     */
    public T findById(K id);
    
    /**
     * 获取实体集合
     * @param entity
     * @return
     */
    public List<T> find(T entity);
    
    /**
     * 通过主键删除实体
     * @param id
     * @return
     */
    public long deleteById(K id);
    
    
    /**
     * 批量删除
     * @param ids
     * @return
     */
    public long batchDeleteByIds(K[] ids);
    
    	
	/**
	 * 全部插入
	 * @param entity
	 * @return  返回插入的个数
	 */
    public long insert(T entity);
    
    /**
	 * 选择性插入
	 * @param entity
	 * @return  返回插入的个数
	 */
    public long insertSelective(T entity);
    
    /**
     * 全部更新
     * @param entity
     * @return
     */
    public long updateById(T entity);
          
    /**
     * 选择性更新
     * @param entity
     * @return
     */
    public long updateByIdSelective(T entity);
    
    /****************************分页、排序******************************/
    /**
     * 根据条件查询所有
     * 条件 + 分页 + 排序
     *
     * @param searchable
     * @return
     */
    public List<T> findAll(Searchable searchable);
    
    /**
     * 根据条件统计所有记录数
     * @param searchable
     * @return
     */
    public long count(Searchable searchable);
    
}
