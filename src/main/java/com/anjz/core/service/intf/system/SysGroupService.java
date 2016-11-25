package com.anjz.core.service.intf.system;

import java.util.List;
import java.util.Set;

import com.anjz.base.service.BaseService;
import com.anjz.core.model.SysGroup;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午10:36:44
 */
public interface SysGroupService extends BaseService<SysGroup, String> {

	/**
     * 获取可用的的分组编号列表
     *
     * @param userId
     * @param organizationIds
     * @return
     */
    public Set<String> findShowGroupIds(String userId, Set<String> organizationIds);
    
    
    /**
     * 批量更新分组数据
     * @param groups
     * @return
     */
    public BaseResult batchUpdate(List<SysGroup> groups);
}
