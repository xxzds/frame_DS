package com.anjz.core.model;
 
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import com.anjz.base.entity.BaseEntity;
import com.anjz.core.enums.UserStatus;

/**
 * 用户表
 * @author ding.shuai
 * @date 2016年9月5日上午8:58:14
 */
public class SysUser extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public static final String USERNAME_PATTERN = "^[\\u4E00-\\u9FA5\\uf900-\\ufa2d_a-zA-Z][\\u4E00-\\u9FA5\\uf900-\\ufa2d\\w]{1,19}$";
    public static final String EMAIL_PATTERN = "^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?";
    public static final String MOBILE_PHONE_NUMBER_PATTERN = "^0?1[3|4|5|8][0-9]\\d{8}$";
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 50;

	/**
	 * 用户名
	 */
	@NotNull(message = "{not.null}")
	@Pattern(regexp = USERNAME_PATTERN, message = "{user.username.not.valid}")
	private java.lang.String userName;

	/**
	 * 密码
	 */
	 @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "{user.password.not.valid}")
	private java.lang.String userPassword;

	/**
	 * 盐
	 */
	private java.lang.String userSalt;

	/**
	 * 用户电话
	 */
	@NotEmpty(message = "{not.null}")
    @Pattern(regexp = MOBILE_PHONE_NUMBER_PATTERN, message = "{user.mobile.phone.number.not.valid}")
	private java.lang.String userPhone;

	/**
	 * 用户邮箱
	 */
	@NotEmpty(message = "{not.null}")
    @Pattern(regexp = EMAIL_PATTERN, message = "{user.email.not.valid}")
	private java.lang.String userEmail;

	/**
	 * 用户创建时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private java.util.Date userCreateTime;

	/**
	 * 用户状态 normal-正常状态  blocked-封禁状态
	 */
	private UserStatus status;
	
	/**
	 * 用户是否删除 0-未删除   1-删除
	 */
	private java.lang.Boolean deleted;
	
	public void setUserName(java.lang.String userName) {
		this.userName = userName;
	}

	public java.lang.String getUserName() {
		return this.userName;
	}
	
	public void setUserPassword(java.lang.String userPassword) {
		this.userPassword = userPassword;
	}

	public java.lang.String getUserPassword() {
		return this.userPassword;
	}
	
	public void setUserSalt(java.lang.String userSalt) {
		this.userSalt = userSalt;
	}

	public java.lang.String getUserSalt() {
		return this.userSalt;
	}
	
	public void setUserPhone(java.lang.String userPhone) {
		this.userPhone = userPhone;
	}

	public java.lang.String getUserPhone() {
		return this.userPhone;
	}
	
	public void setUserEmail(java.lang.String userEmail) {
		this.userEmail = userEmail;
	}

	public java.lang.String getUserEmail() {
		return this.userEmail;
	}
	
	public void setUserCreateTime(java.util.Date userCreateTime) {
		this.userCreateTime = userCreateTime;
	}

	public java.util.Date getUserCreateTime() {
		return this.userCreateTime;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public java.lang.Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(java.lang.Boolean deleted) {
		this.deleted = deleted;
	}
}