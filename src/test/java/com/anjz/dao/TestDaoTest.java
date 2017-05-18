package com.anjz.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.dao.TestDao;

/**
 * 测试
 * @author shuai.ding
 *
 * @date 2017年5月18日下午5:06:23
 */
public class TestDaoTest extends BaseTest{

	@Resource
	private TestDao testDao;
	
	@Test
	public void testQueryUserByName(){
		testDao.queryUserByName("test",new Date());
	}
	
}
