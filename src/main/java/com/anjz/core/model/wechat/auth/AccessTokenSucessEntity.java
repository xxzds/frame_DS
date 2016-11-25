package com.anjz.core.model.wechat.auth;

/**
 * @author ding.shuai
 * @date 2016年9月28日下午4:02:56
 */
public class AccessTokenSucessEntity {

	/**
	 * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
	 */
	private String access_token;
	
	/**
	 * access_token接口调用凭证超时时间，单位（秒）
	 */
	private Long expires_in;
	
	/**
	 * 用户刷新access_token
	 */
	private String refresh_token;
	
	/**
	 * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
	 */
	private String openid;
	
	/**
	 * 用户授权的作用域，使用逗号（,）分隔
	 */
	private String scope;

	public String getAccess_token() {
		return access_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public Long getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Long expires_in) {
		this.expires_in = expires_in;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
}
