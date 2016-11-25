package com.anjz.core.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.anjz.base.dao.BaseDao;
import com.anjz.core.model.SysAuth;

/**
 * @author ding.shuai
 * @date 2016年8月26日上午11:56:04
 */
public interface SysAuthDao extends BaseDao<SysAuth,String> {
	
	/**
     * 根据用户信息获取角色role_ids集合
     * 1.1、用户  根据用户绝对匹配
     * 1.2、组织机构 根据组织机构绝对匹配 此处需要注意 祖先需要自己获取
     * 1.3、工作职务 根据工作职务绝对匹配 此处需要注意 祖先需要自己获取
     * 1.4、组织机构和工作职务  根据组织机构和工作职务绝对匹配 此处不匹配祖先
     * 1.5、组  根据组绝对匹配
     *
     * @param userId             必须有
     * @param groupIds           可选
     * @param organizationIds    可选
     * @param jobIds             可选
     * @param organizationJobIds 可选
     * @return
     */
	 public List<String> findRoleIds(@Param("userId")String userId, @Param("groupIds")Set<String> groupIds, @Param("organizationIds")Set<String> organizationIds, 
			 @Param("jobIds")Set<String> jobIds, @Param("organizationJobIds")Set<String[]> organizationJobIds);

}