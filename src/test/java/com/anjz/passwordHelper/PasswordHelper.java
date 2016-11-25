package com.anjz.passwordHelper;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.PasswordHelperService;

public class PasswordHelper extends BaseTest {
	
	@Resource
	private PasswordHelperService passwordHelperService;
	
	
	@Test
	public void test(){
		SysUser user=new SysUser();
		user.setUserName("admin");
		user.setUserPassword("1");
		passwordHelperService.encryptPassword(user);
		System.out.println(user.getUserSalt());
		System.out.println(user.getUserPassword());
	}

}
