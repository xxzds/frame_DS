package com.anjz.core.model.wechat;

/**
 * 调用接口失败时，返回的数据
 * @author ding.shuai
 * @date 2016年9月28日下午4:00:57
 */
public class FailEntity {
	/**
	 * 错误的状态码
	 */
	private Integer errcode;
	
	/**
	 * 错误信息
	 */
	private String  errmsg;

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
}
