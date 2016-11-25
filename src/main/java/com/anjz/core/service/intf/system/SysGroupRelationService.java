package com.anjz.core.service.intf.system;

import java.util.Set;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.SysGroup;
import com.anjz.core.model.SysGroupRelation;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:42:37
 */
public interface SysGroupRelationService extends BaseService<SysGroupRelation, String>{

	/**
	 * 查询某用户所在的组集合
	 * @param userId  用户ID
	 * @param organizationIds  用户所在组织机构集合
	 * @return
	 */
	 public Set<String> findGroupIds(String userId, Set<String> organizationIds);
	 
	 
	 /**
	  * 添加组中的成员
	  * @param group
	  * @param ids  用户id/组织id
	  * @return
	  */
	 public BaseResult appendRelation(SysGroup group,String[] ids);
	 
	 
	 /**
	  * 删除组中的成员
	  * @param group
	  * @param ids 用户id/组织id
	  * @return
	  */
	 public BaseResult deleteRelation(SysGroup group,String[] ids);
}
