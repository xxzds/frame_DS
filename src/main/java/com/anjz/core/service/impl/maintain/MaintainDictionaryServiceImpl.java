package com.anjz.core.service.impl.maintain;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.entity.search.pageandsort.Sort.Direction;
import com.anjz.base.service.BaseTreeableServiceImpl;
import com.anjz.core.dao.MaintainDictionaryDao;
import com.anjz.core.model.MaintainDictionary;
import com.anjz.core.service.intf.maintain.MaintainDictionaryService;
import com.anjz.result.BaseResult;
import com.anjz.result.ListResult;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年9月13日下午5:42:09
 */
@Service
public class MaintainDictionaryServiceImpl extends BaseTreeableServiceImpl<MaintainDictionary, String> implements MaintainDictionaryService{
	
	@Resource
	private MaintainDictionaryDao maintainDictionaryDao;
	
	/**
	 * 通过父编码查询子节点的字典集合,默认按weight的升序排列(可用)
	 * @param parentCode
	 * @return
	 */
	@Cacheable(value="ditionaryCache", key="'parentcode-'+#parentCode")
	public ListResult<MaintainDictionary> findDictionariesByParentCode(String parentCode){	
		ListResult<MaintainDictionary> result=new ListResult<MaintainDictionary>();
		
		Searchable searchable=new SearchRequest().addSearchFilter("name_code", SearchOperator.eq, parentCode);
		
		List<MaintainDictionary> parentLists= this.findAllWithNoPageNoSort(searchable);
		if(parentLists.isEmpty() && !parentLists.get(0).getIsShow()){
			return result;
		}
		MaintainDictionary parent= parentLists.get(0);
		
		searchable.removeSearchFilter("name_code", SearchOperator.eq)
				.addSearchFilter("parent_id", SearchOperator.eq, parent.getId())
				.addSearchFilter("is_show", SearchOperator.eq, true)
				.addSort(Direction.ASC, "weight");
		
		result.setData(this.findAllWithSort(searchable));
		return result;	
	}
	
	
	/**
	 * 通过编码查询实体
	 * @param nameCode
	 * @return
	 */
	@Cacheable(value="ditionaryCache", key="'selfcode-'+#nameCode")
	public PlainResult<MaintainDictionary> findDictinaryByCode(String nameCode){
		PlainResult<MaintainDictionary> result=new PlainResult<MaintainDictionary>();
		
		MaintainDictionary dictionary = new MaintainDictionary();
		dictionary.setNameCode(nameCode);
		List<MaintainDictionary> list= this.find(dictionary).getData();
		if(list.isEmpty() && !list.get(0).getIsShow()){
			return result;
		}
		result.setData(list.get(0));		
		return result;
	}
	
	
	
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public void deleteSelfAndChild(MaintainDictionary m) {
		super.deleteSelfAndChild(m);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public void deleteSelfAndChild(List<MaintainDictionary> mList) {
		super.deleteSelfAndChild(mList);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public void appendChild(MaintainDictionary parent, MaintainDictionary child) {
		super.appendChild(parent, child);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public void move(MaintainDictionary source, MaintainDictionary target, String moveType) {
		super.move(source, target, moveType);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public BaseResult deleteById(String id) {
		return super.deleteById(id);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public BaseResult batchDeleteByIds(String[] ids) {
		return super.batchDeleteByIds(ids);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public BaseResult saveAll(MaintainDictionary m) {
		return super.saveAll(m);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public BaseResult saveSelective(MaintainDictionary m) {
		return super.saveSelective(m);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public BaseResult updateAllById(MaintainDictionary m) {
		return super.updateAllById(m);
	}
	
	@Override
	@CacheEvict(value="ditionaryCache",allEntries=true)
	public BaseResult updateSelectiveById(MaintainDictionary m) {
		return super.updateSelectiveById(m);
	}
	
}
