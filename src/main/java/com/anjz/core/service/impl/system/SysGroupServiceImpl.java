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
import com.anjz.core.model.SysGroup;
import com.anjz.core.model.SysGroupRelation;
import com.anjz.core.service.intf.system.SysGroupRelationService;
import com.anjz.core.service.intf.system.SysGroupService;
import com.anjz.exception.BusinessException;
import com.anjz.result.BaseResult;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:37:11
 */
@Service
public class SysGroupServiceImpl extends BaseServiceImpl<SysGroup, String>implements SysGroupService{
	
	@Resource
	private SysGroupRelationService sysGroupRelationService;

	 /**
     * 获取可用的的分组编号列表
     *
     * @param userId
     * @param organizationIds
     * @return
     */
    public Set<String> findShowGroupIds(String userId, Set<String> organizationIds) {
        Set<String> groupIds = Sets.newHashSet();
        groupIds.addAll(sysGroupRelationService.findGroupIds(userId, organizationIds));

        //TODO 如果分组数量很多 建议此处查询时直接带着是否可用的标识去查
        for (SysGroup group : find(null).getData()) {
            if (Boolean.FALSE.equals(group.getIsShow())) {
                groupIds.remove(group.getId());
            }
        }
        return groupIds;
    }
    
    /**
     * 批量更新分组数据
     * @param groups
     * @return
     */
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult batchUpdate(List<SysGroup> groups){
    	for(SysGroup group:groups){
    		BaseResult result= this.updateSelectiveById(group);
    		if(!result.isSuccess()){
    			throw new BusinessException("更新分组数据失败");
    		}
    	}
    	
    	return new BaseResult();
    }
    
    
    /**
     * 重写批量删除的方法
     */
    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public BaseResult batchDeleteByIds(String[] ids) {
    	
    	if(ids==null || ids.length==0){
    		return new BaseResult();
    	}
    	
    	//删除分组表的关联表
    	Searchable searchable=new SearchRequest()
    			.addSearchFilter("group_id",SearchOperator.in , ids);
    	List<SysGroupRelation> list= sysGroupRelationService.findAllWithNoPageNoSort(searchable);
    	
    	if(!list.isEmpty()){
    		Set<String> groupRelationIds=Sets.newHashSet(
    				Lists.transform(list, new Function<SysGroupRelation, String>() {
    					@Override
    					public String apply(SysGroupRelation input) {
    						return input.getId();
    					}}));
    		
    		BaseResult result= sysGroupRelationService.batchDeleteByIds(groupRelationIds.toArray(new String[]{}));
    		if(!result.isSuccess()){
    			throw new BusinessException("删除分组表的关联表失败");
    		}
    	}
    	
    	
    	return super.batchDeleteByIds(ids);
    }
}
