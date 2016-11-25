package com.anjz.base.service;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.anjz.base.dao.BaseDao;
import com.anjz.base.entity.BaseEntity;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Page;
import com.anjz.base.entity.search.pageandsort.PageImpl;
import com.anjz.base.entity.search.pageandsort.Pageable;
import com.anjz.base.entity.search.pageandsort.Sort;
import com.anjz.result.BaseResult;
import com.anjz.result.ListResult;
import com.anjz.result.PlainResult;
import com.anjz.util.UuidUtil;

/**
 *<p> 抽象service层基类 提供一些简便方法
 *<p> 泛型 ： M 表示实体类型；ID表示主键类型
 * @author ding.shuai
 * @date 2016年8月22日上午9:37:23
 */
public class BaseServiceImpl<M extends BaseEntity,ID extends Serializable> implements BaseService<M, ID> {
	
	@Autowired
	protected BaseDao<M, ID> baseDao;
	
	
	/**
	 * 通过主键查询
	 * @param id
	 * @return
	 */
	@Override
	public PlainResult<M> findOne(ID id){
		PlainResult<M> result=new PlainResult<M>();
		M m= baseDao.findById(id);
		result.setData(m);
		return result;
	}
	
	/**
	 * 通过条件查询实体集合
	 * @param m
	 * @return
	 */
	@Override
	public ListResult<M> find(M m){
		ListResult<M> result=new ListResult<M>();
		result.setData(baseDao.find(m));
		return result;
	}
	
	/**
	 * 查询所有数据
	 * @return
	 */
	public ListResult<M> findAll(){
		return this.find(null);
	}
	
	
	/**
	 * 通过主键删除
	 * @param id
	 * @return
	 */
	@Override
	public BaseResult deleteById(ID id){
		BaseResult result=new BaseResult();
		long count=baseDao.deleteById(id);
		if(count<0){
			result.setErrorMessage(500, "删除数据失败");
		}
		return result;		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@Override
	public BaseResult batchDeleteByIds(ID[] ids){
		BaseResult result=new BaseResult();
		long count=baseDao.batchDeleteByIds(ids);
		if(count<0){
			result.setErrorMessage(500, "批量删除数据失败");
		}
		return result;
	}
	
	
	/**
	 * 保存单个实体(包括空字段)
	 * @param m
	 * @return
	 */
	@Override
	public BaseResult saveAll(M m) {
		BaseResult result = new BaseResult();
		//设置主键
		if(StringUtils.isEmpty(m.getId())){
			m.setId(UuidUtil.generateUuid32());
		}
		long count = baseDao.insert(m);
		if (count <= 0) {
			result.setErrorMessage(500, "插入数据失败");
		}
		return result;
	}
	
	
	/**
	 * 保存单个实体(过滤掉空字段)
	 * @param m
	 * @return
	 */
	@Override
	public BaseResult saveSelective(M m){
		BaseResult result = new BaseResult();
		//设置主键
		if(StringUtils.isEmpty(m.getId())){
			m.setId(UuidUtil.generateUuid32());
		}
		long count = baseDao.insertSelective(m);
		if (count <= 0) {
			result.setErrorMessage(500, "插入数据失败");
		}
		return result;
	}
	
	
	/**
	 * 通过主键更新所有数据
	 * @param m
	 * @return
	 */
	@Override
	public BaseResult updateAllById(M m){
		BaseResult result = new BaseResult();
		long count =baseDao.updateById(m);
		if (count <= 0) {
			result.setErrorMessage(500, "更新数据失败");
		}
		return result;
	}
	
	/**
	 * 通过主键更新非空数据
	 * @param m
	 * @return
	 */
	@Override
	public BaseResult updateSelectiveById(M m){
		BaseResult result = new BaseResult();
		long count =baseDao.updateByIdSelective(m);
		if (count <= 0) {
			result.setErrorMessage(500, "更新数据失败");
		}
		return result;
	}
	
	
	/********************************分页、排序***********************************/
	
	
	/**
     * 按照顺序查询所有实体
     *
     * @param sort
     * @return
     */
	@Override
    public List<M> findAll(Sort sort) {
        Searchable searchable=new SearchRequest(null, sort);
        return baseDao.findAll(searchable);
    }

    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return
     */
	@Override
    public Page<M> findAll(Pageable pageable) {
		Searchable searchable=new SearchRequest(null, pageable);
        return this.findAll(searchable);
    }

    /**
     * 按条件分页并排序查询实体
     *
     * @param searchable 条件
     * @return
     */
	@Override
    public Page<M> findAll(Searchable searchable) {
		List<M> list=baseDao.findAll(searchable);
		long total = searchable.hasPageable() ? count(searchable) : list.size();
		return new PageImpl<M>(
                list,
                searchable.getPage(),
                total
        );
    }

    /**
     * 按条件不分页不排序查询实体
     *
     * @param searchable 条件
     * @return
     */
	@Override
    public List<M> findAllWithNoPageNoSort(Searchable searchable) {
        searchable.removePageable();
        searchable.removeSort();
        return baseDao.findAll(searchable);
    }

    /**
     * 按条件排序查询实体(不分页)
     *
     * @param searchable 条件
     * @return
     */
    public List<M> findAllWithSort(Searchable searchable) {
        searchable.removePageable();
        return baseDao.findAll(searchable);
    }


    /**
     * 按条件分页并排序统计实体数量
     *
     * @param searchable 条件
     * @return
     */
    @Override
    public Long count(Searchable searchable) {
        return baseDao.count(searchable);
    }
    
    
}
