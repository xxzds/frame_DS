package com.anjz.sysAuth;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.dao.SysAuthDao;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.SysAuthService;
import com.google.common.collect.Sets;

/**
 * @author ding.shuai
 * @date 2016年8月26日下午12:14:40
 */
public class SysAuthTest extends BaseTest{
	
	@Resource
	private SysAuthService sysAuthService;

	@Resource
	private SysAuthDao sysAuthDao;
	
	@Test
	public void test(){
		Set<String> groupIds=Sets.newHashSet();
		groupIds.add("1");
		groupIds.add("2");
		
		Set<String> organizationIds=Sets.newHashSet();
		organizationIds.add("1");
		
		Set<String> jobIds=Sets.newHashSet();
		jobIds.add("1");
		
		Set<String[]> organizationJobIds=Sets.newHashSet();
		organizationJobIds.add(new String[]{"6","7"});
		organizationJobIds.add(new String[]{"9","10"});
		
		List<String> list=sysAuthDao.findRoleIds("1", groupIds, organizationIds, jobIds, organizationJobIds);
		for(String str:list){
			System.out.println(str);
		}
	}
	
	
	@Test
	public void findStringRolesTest(){
		SysUser user=new SysUser();
		user.setId("1");
		Set<String> sets=sysAuthService.findStringRoles(user).getData();
		for(String str:sets){
			System.out.println(str);
		}
	}
	
	@Test
	public void findStringPermissionsTest(){
		SysUser user=new SysUser();
		user.setId("1");
		Set<String> sets=sysAuthService.findStringPermissions(user).getData();
		System.out.println("--------------------");
		for(String str:sets){
			System.out.println(str);
		}
	}
}
