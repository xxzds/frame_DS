package com.anjz.core.service.impl.system;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.anjz.base.entity.search.SearchOperator;
import com.anjz.base.entity.search.SearchRequest;
import com.anjz.base.entity.search.Searchable;
import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.dao.SysGroupRelationDao;
import com.anjz.core.enums.GroupType;
import com.anjz.core.model.SysGroup;
import com.anjz.core.model.SysGroupRelation;
import com.anjz.core.service.intf.system.SysGroupRelationService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:43:40
 */
@Service
public class SysGroupRelationServiceImpl extends BaseServiceImpl<SysGroupRelation, String>
		implements SysGroupRelationService {

	@Resource
	private SysGroupRelationDao sysGroupRelationDao;

	/**
	 * 查询某用户所在的组集合
	 * 
	 * @param userId
	 *            用户ID
	 * @param organizationIds
	 *            用户所在组织机构集合
	 * @return
	 */
	@Override
	public Set<String> findGroupIds(String userId, Set<String> organizationIds) {

		return Sets.newHashSet(sysGroupRelationDao.findGroupIds(userId, organizationIds));
	}

	/**
	 * 添加组中的成员
	 * 
	 * @param group
	 * @param ids
	 * @return
	 */
	@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
	public BaseResult appendRelation(SysGroup group, String[] ids) {
		
		if(group==null){
			throw new BusinessException("添加成员，数据异常");
		}

		for (String id : ids) {
			SysGroupRelation sysGroupRelation = new SysGroupRelation();
			sysGroupRelation.setGroupId(group.getId());
			
			if (group.getType() == GroupType.user) {
				sysGroupRelation.setUserId(id);
			} else if (group.getType() == GroupType.organization) {
				sysGroupRelation.setOrganizationId(id);
			}
			
			//查询数据库中是否已有数据,如果有，不需要插入数据库
			List<SysGroupRelation> list= this.find(sysGroupRelation).getData();
			if(!list.isEmpty()){
				continue;
			}
			BaseResult baseResult= this.saveSelective(sysGroupRelation);
			if(!baseResult.isSuccess()){
				throw new BusinessException("添加成员失败");
			}
		}

		return new BaseResult();
	}
	
	/**
	  * 删除组中的成员
	  * @param group
	  * @param ids 用户id/组织id
	  * @return
	  */
	public BaseResult deleteRelation(SysGroup group, String[] ids) {
		if (group == null) {
			throw new BusinessException("删除成员，数据异常");
		}

		Searchable searchable=new SearchRequest()
				.addSearchFilter("group_id", SearchOperator.eq, group.getId());
		if(group.getType() == GroupType.user){
			searchable.addSearchFilter("user_id", SearchOperator.in, ids);
		}else if(group.getType() == GroupType.organization){
			searchable.addSearchFilter("organization_id", SearchOperator.in, ids);
		}else{
			throw new BusinessException("没有找到分组类型");
		}
		
		List<SysGroupRelation> list= this.findAllWithNoPageNoSort(searchable);
		
		Set<String> groupRelationIds=Sets.newHashSet(
				Lists.transform(list, new Function<SysGroupRelation, String>() {
					@Override
					public String apply(SysGroupRelation input) {
						return input.getId();
					}
				}));
		
		if(groupRelationIds.isEmpty()){
			return new BaseResult();
		}
		
		BaseResult baseResult= this.batchDeleteByIds(groupRelationIds.toArray(new String[]{}));
		if(!baseResult.isSuccess()){
			throw new BusinessException("删除分组成员失败");
		}		
		return new BaseResult();
	}
}
