package com.anjz.base.service;

import java.io.Serializable;
import java.util.List;

import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.result.BaseResult;
import com.anjz.result.ListResult;
import com.anjz.result.PlainResult;

/**
 *<p> 抽象service层基类 提供一些简便方法
 *<p> 泛型 ： M 表示实体类型；ID表示主键类型
 * @author ding.shuai
 * @date 2016年8月22日上午9:37:23
 */
public interface BaseService<M extends BaseEntity,ID extends Serializable> {
	
	
	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	public PlainResult<M> findOne(ID id);
	
	/**
	 * 通过条件查询实体集合
	 * @param m
	 * @return
	 */
	public ListResult<M> find(M m);
	
	
	/**
	 * 查询所有数据
	 * @return
	 */
	public ListResult<M> findAll();
	
	
	/**
	 * 通过主键删除
	 * @param id
	 * @return
	 */
	public BaseResult deleteById(ID id);
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	public BaseResult batchDeleteByIds(ID[] ids);
	
	
	/**
	 * 保存单个实体(包括空字段)
	 * @param m
	 * @return
	 */
	public BaseResult saveAll(M m);
	
	
	/**
	 * 保存单个实体(过滤掉空字段)
	 * @param m
	 * @return
	 */
	public BaseResult saveSelective(M m);
	
	
	/**
	 * 通过主键更新所有数据
	 * @param m
	 * @return
	 */
	public BaseResult updateAllById(M m);
	
	/**
	 * 通过主键更新非空数据
	 * @param m
	 * @return
	 */
	public BaseResult updateSelectiveById(M m);	
	
	
	/*******************************************分页、排序***********************************/
	
	/**
     * 按照顺序查询所有实体
     *
     * @param sort
     * @return
     */
    public List<M> findAll(Sort sort);

    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return
     */
    public Page<M> findAll(Pageable pageable);

    /**
     * 按条件分页并排序查询实体
     *
     * @param searchable 条件
     * @return
     */
    public Page<M> findAll(Searchable searchable);

    /**
     * 按条件不分页不排序查询实体
     *
     * @param searchable 条件
     * @return
     */
    public List<M> findAllWithNoPageNoSort(Searchable searchable);

    /**
     * 按条件排序查询实体(不分页)
     *
     * @param searchable 条件
     * @return
     */
    public List<M> findAllWithSort(Searchable searchable);


    /**
     * 按条件分页并排序统计实体数量
     *
     * @param searchable 条件
     * @return
     */
    public Long count(Searchable searchable);
}
