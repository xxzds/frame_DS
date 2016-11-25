package com.anjz.core.service.intf.system;

import com.anjz.core.model.SysUser;

/**
 * 生成盐，密码加密
 * @author ding.shuai
 * @date 2016年8月19日下午5:02:03
 */
public interface PasswordHelperService {
	/**
	 * 密码加密
	 * @param user
	 */
	public void encryptPassword(SysUser user);
	
	 /**
     * 密码加密
     * @param username  用户名
     * @param password  明文密码
     * @param salt 盐值
     * @return
     */
    public String encryptPassword(String username, String password, String salt);
    
    
    /**
     * 密码是否匹配
     * @param user   用户
     * @param newPassword 用户的明文密码
     * @return
     */
    public boolean matches(SysUser user, String newPassword);
}
