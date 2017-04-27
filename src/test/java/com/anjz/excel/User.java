package com.anjz.excel;

/**
 * 用户实体
 * @author shuai.ding
 *
 * @date 2017年4月27日下午3:15:27
 */
public class User {
	private String userId;
	private String userName;
	private Integer userAge;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getUserAge() {
		return userAge;
	}
	public void setUserAge(Integer userAge) {
		this.userAge = userAge;
	}
	
}
