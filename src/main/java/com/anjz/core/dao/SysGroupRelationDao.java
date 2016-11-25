package com.anjz.core.dao;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.anjz.base.dao.BaseDao;
import com.anjz.core.model.SysGroupRelation;

public interface SysGroupRelationDao extends BaseDao<SysGroupRelation, String> {

	public List<String> findGroupIds(@Param("userId")String userId, @Param("organizationIds")Set<String> organizationIds);
}