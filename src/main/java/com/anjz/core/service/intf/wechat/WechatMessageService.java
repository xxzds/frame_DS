package com.anjz.core.service.intf.wechat;

/**
 * 微信xml消息推送的服务
 * @author ding.shuai
 * @date 2016年9月27日下午10:04:26
 */
public interface WechatMessageService {
	
	/**
	 * 处理xml数据
	 * @param xml  微信传入的数据
	 * @return  返回xml格式的数据
	 */
	public String  processWechatMag(String xml);

}
