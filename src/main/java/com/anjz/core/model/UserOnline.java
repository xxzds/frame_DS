package com.anjz.core.model;

import java.util.Date;

import com.anjz.shiro.session.MySession;

/**
 * 用户在线实体
 * @author ding.shuai
 * @date 2016年9月12日下午7:46:06
 */
public class UserOnline {
	
	/**
	 * sessionID
	 */
	private String id;
	
	/**
	 * 用户Id
	 */
	private String userId;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	
	/**
	 * User-Agent
	 */
	private String userAgent;
	
	/**
	 * ip+端口
	 */
	private String systemHost;
	
	/**
	 * 用户的状态
	 */
	private MySession.OnlineStatus status;
	
	/**
	 * 主机地址
	 */
	private String host;
	
	/**
	 * 超时时间
	 */
	private String timeout;
	
	/**
	 * 最后访问的时间
	 */
	private Date lastAccessTime;
	
	/**
	 * 第一次访问的时间
	 */
	private Date startTimestamp;
	
	/**
	 * 停止session使用的时间
	 */
	private Date stopTimestamp;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getSystemHost() {
		return systemHost;
	}

	public void setSystemHost(String systemHost) {
		this.systemHost = systemHost;
	}

	public MySession.OnlineStatus getStatus() {
		return status;
	}

	public void setStatus(MySession.OnlineStatus status) {
		this.status = status;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getTimeout() {
		return timeout;
	}

	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}

	public Date getLastAccessTime() {
		return lastAccessTime;
	}

	public void setLastAccessTime(Date lastAccessTime) {
		this.lastAccessTime = lastAccessTime;
	}

	public Date getStartTimestamp() {
		return startTimestamp;
	}

	public void setStartTimestamp(Date startTimestamp) {
		this.startTimestamp = startTimestamp;
	}

	public Date getStopTimestamp() {
		return stopTimestamp;
	}

	public void setStopTimestamp(Date stopTimestamp) {
		this.stopTimestamp = stopTimestamp;
	}
}
