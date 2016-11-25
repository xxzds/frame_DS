package com.anjz.core.service.impl.system;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.anjz.base.service.BaseServiceImpl;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.core.service.intf.system.SysUserOrganizationJobService;
import com.anjz.result.ListResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @author ding.shuai
 * @date 2016年8月25日下午4:45:48
 */
@Service
public class SysUserOrganizationJobServiceImpl extends BaseServiceImpl<SysUserOrganizationJob, String> implements SysUserOrganizationJobService{

	/**
	 * 通过用户Id,查询集合列表
	 * @param userId
	 * @return
	 */
	@Override
	public ListResult<SysUserOrganizationJob> findOrganizationJobByUserId(String userId) {		
		if(StringUtils.isEmpty(userId)){
			return new ListResult<SysUserOrganizationJob>();
		}
		
		SysUserOrganizationJob model=new SysUserOrganizationJob();
		model.setUserId(userId);
		return this.find(model);
	}
	
	
	 /**
	  * list->map
     * 组织机构id 对应多个工作职位
     * @param userOrganizationJobs
     * @return
     */
    public Map<String, List<SysUserOrganizationJob>> getDisplayOrganizationJobs(
    		List<SysUserOrganizationJob> userOrganizationJobs){
    	
    	if(userOrganizationJobs.isEmpty()){
    		return null;
    	}
    	
    	//组织机构和工作职位是一对多的关系
		Map<String, List<SysUserOrganizationJob>> organizationJobsMap=Maps.newHashMap();
		
		for(SysUserOrganizationJob sysUserOrganizationJob:userOrganizationJobs){
			String organizationId= sysUserOrganizationJob.getOrganizationId();
			
			List<SysUserOrganizationJob> subList= organizationJobsMap.get(organizationId);
			
			if(subList==null){
				subList=Lists.newArrayList();
				organizationJobsMap.put(organizationId, subList);
			}
			subList.add(sysUserOrganizationJob);
		}
    	
    	return organizationJobsMap;
    }

}
