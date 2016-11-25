package com.anjz.core.service.impl.system;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.anjz.core.model.SysUser;
import com.anjz.core.service.intf.system.PasswordHelperService;

/**
 * @author ding.shuai
 * @date 2016年8月19日下午5:05:07
 */
@Service
public class PasswordHelperServiceImpl implements PasswordHelperService{
	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

    @Value("${password.algorithmName}")
    private String algorithmName = "md5";
    @Value("${password.hashIterations}")
    private int hashIterations = 2;

    public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
        this.randomNumberGenerator = randomNumberGenerator;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public void setHashIterations(int hashIterations) {
        this.hashIterations = hashIterations;
    }

    /**
	 * 密码加密
	 * @param user
	 */
    @Override
    public void encryptPassword(SysUser user) {

        user.setUserSalt(randomNumberGenerator.nextBytes().toHex());

        String newPassword = new SimpleHash(
                algorithmName,
                user.getUserPassword(),
                ByteSource.Util.bytes(user.getUserName()+user.getUserSalt()),
                hashIterations).toHex();

        user.setUserPassword(newPassword);
    }
    
    
    
    /**
     * 密码加密
     * @param username  用户名
     * @param password  明文密码
     * @param salt 盐值
     * @return
     */
    public String encryptPassword(String username, String password, String salt) {
        return new SimpleHash(
                algorithmName,
                password,
                ByteSource.Util.bytes(username+salt),
                hashIterations).toHex();
    }
    
    
    /**
     * 密码是否匹配
     * @param user   用户
     * @param newPassword 用户的明文密码
     * @return
     */
    public boolean matches(SysUser user, String newPassword) {
        return user.getUserPassword().equals(encryptPassword(user.getUserName(), newPassword, user.getUserSalt()));
    }
}
