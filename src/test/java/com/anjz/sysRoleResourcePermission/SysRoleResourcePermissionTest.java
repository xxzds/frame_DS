package com.anjz.sysRoleResourcePermission;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.model.SysRoleResourcePermission;
import com.anjz.core.service.intf.system.SysRoleResourcePermissionService;

public class SysRoleResourcePermissionTest extends BaseTest{

	@Resource
	private SysRoleResourcePermissionService sysRoleResourcePermissionService;

	
	@Test
	public void test(){
//		SysRoleResourcePermission model= sysRoleResourcePermissionService.findOne("1").getData();
//		System.out.println(model.getResourceId());
		
		sysRoleResourcePermissionService.find(null);
	}
}
