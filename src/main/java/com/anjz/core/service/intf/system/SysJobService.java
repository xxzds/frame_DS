package com.anjz.core.service.intf.system;

import java.util.Set;

import com.anjz.base.service.BaseTreeableService;
import com.anjz.core.model.SysJob;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:17:21
 */
public interface SysJobService extends BaseTreeableService<SysJob, String>{

	/**
     * 过滤仅获取可显示的数据
     *
     * @param jobIds
     * @param organizationJobIds
     */
    public void filterForCanShow(Set<String> jobIds, Set<String[]> organizationJobIds);
}
