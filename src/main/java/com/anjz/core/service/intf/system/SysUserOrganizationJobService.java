package com.anjz.core.service.intf.system;

import java.util.List;
import java.util.Map;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.result.ListResult;


/**
 * @author ding.shuai
 * @date 2016年8月25日下午4:45:00
 */
public interface SysUserOrganizationJobService extends BaseService<SysUserOrganizationJob, String> {
	
	
	/**
	 * 通过用户Id,查询集合列表
	 * @param userId
	 * @return
	 */
	public ListResult<SysUserOrganizationJob> findOrganizationJobByUserId(String userId);
	
	/**
	  * list->map
    * 组织机构id 对应多个工作职位
    * @param userOrganizationJobs
    * @return
    */
	public Map<String, List<SysUserOrganizationJob>> getDisplayOrganizationJobs(List<SysUserOrganizationJob> userOrganizationJobs);

}
