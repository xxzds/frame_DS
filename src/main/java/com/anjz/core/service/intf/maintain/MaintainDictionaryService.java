package com.anjz.core.service.intf.maintain;

import com.anjz.base.service.BaseTreeableService;
import com.anjz.core.model.MaintainDictionary;
import com.anjz.result.ListResult;
import com.anjz.result.PlainResult;

/**
 * @author ding.shuai
 * @date 2016年9月13日下午5:42:09
 */
public interface MaintainDictionaryService extends BaseTreeableService<MaintainDictionary, String>{
	
	
	/**
	 * 通过父编码查询子节点的字典集合,默认按weight的升序排列
	 * @param parentCode
	 * @return
	 */
	public ListResult<MaintainDictionary> findDictionariesByParentCode(String parentCode);
	
	
	/**
	 * 通过编码查询实体
	 * @param nameCode
	 * @return
	 */
	public PlainResult<MaintainDictionary> findDictinaryByCode(String nameCode);

}
