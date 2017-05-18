package com.anjz.core.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.anjz.core.model.SysUser;

/**
 * 测试dao
 * @author shuai.ding
 *
 * @date 2017年5月18日下午4:59:59
 */
public interface TestDao {
	public List<SysUser> queryUserByName(@Param("name") String name,@Param("date") Date date);
}
