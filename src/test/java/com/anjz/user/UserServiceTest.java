package com.anjz.user;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.dao.SysUserDao;
import com.anjz.core.enums.UserStatus;
import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.PasswordHelperService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.util.UuidUtil;

/**
 * 
 * 2016年1月1日下午1:14:06
 * ding.shuai
 */

public class UserServiceTest  extends BaseTest{
  @Resource
  private SysUserService sysUserService;
  
  @Resource
  private SysUserDao sysUserDao;
  
  @Resource
  private PasswordHelperService passwordHelperService;
  
  @Test
  public void testSelectAll(){
//	 sysUserService.findByUsername("admin");
	  
	  
//	  sysUserDao.findById("1");
	  
	  
//	  SysUser sysUser=new SysUser();	  
//	  sysUser.setUserName("admin");
//	  sysUserDao.find(sysUser);
	  
	  SysUser sysUser=new SysUser();	  
	  sysUser.setId(UuidUtil.generateUuid32());
	  sysUser.setUserName("ds");
	  sysUser.setUserPassword("1");
	  passwordHelperService.encryptPassword(sysUser);
	  sysUserDao.insert(sysUser);
	  
//	  SysUser sysUser=new SysUser();	  
//	  sysUser.setUserId(UuidUtil.generateUuid32());
//	  sysUser.setUserName("ds2");
//	  sysUser.setUserPassword("1");
//	  passwordHelperService.encryptPassword(sysUser);
//	  sysUserDao.insertSelective(sysUser);
	  
	  
//	  SysUser sysUser=new SysUser();	  
//	  sysUser.setUserName("ds2");
//	  List<SysUser> list= sysUserDao.find(sysUser);
//	  sysUser=list.get(0);
//	  sysUser.setUserName("666");
//	  sysUserDao.updateById(sysUser);
//	  sysUserDao.updateByIdSelective(sysUser);
  }
  
  
  @Test
  public void changePasswordWithAdmin(){
	  sysUserService.changePassword(new String[]{"1"}, "1");
  }
  
  
  /**
   * 保存用户、用户职位
   * @author shuai.ding
   */
  @Test
  public void saveUserAndOrganazionAndJobTest(){
	  SysUser user = new SysUser();
	  user.setUserName("test10");
	  user.setUserPassword("123456");
	  user.setUserPhone("18245678987");
	  user.setUserEmail("121213243@qq.com");
	  user.setStatus(UserStatus.normal);
	  user.setDeleted(true);
	  
	  
	  sysUserService.saveUserAndOrganazionAndJob(user, null);
  }
}
