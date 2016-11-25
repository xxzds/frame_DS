package com.anjz.core.service.intf.system;

import java.util.Set;

import com.anjz.base.service.BaseTreeableService;
import com.anjz.core.model.SysOrganization;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:11:13
 */
public interface SysOrganizationService extends BaseTreeableService<SysOrganization, String>{
	/**
     * 过滤仅获取可显示的数据
     *
     * @param organizationIds
     * @param organizationJobIds
     */
    public void filterForCanShow(Set<String> organizationIds, Set<String[]> organizationJobIds);
}
