package com.anjz.message.mail;

import com.anjz.result.BaseResult;

/**
 * 邮件发送服务
 * @author ding.shuai
 * @date 2016年7月28日下午4:35:58
 */
public interface MailSendService {
	
	/**
	 * 发送文本邮件
	 * @param subject  主题
	 * @param text  内容
	 * @param toAddresses   发送集合
	 * @param ccAddresses   抄送集合
	 * @param bccAddresses  密送集合
	 * @return
	 */
	public BaseResult sendTextMail(String subject,String text,String[] toAddresses,String[] ccAddresses,String[] bccAddresses);
	
	/**
	 * 发送文本邮件
	 * @param subject   主题
	 * @param text   内容
	 * @param toAddress  接收者的用户名
	 * @return
	 */
	public BaseResult sendTextMail(String subject,String text,String toAddress);
	
	
	/**
	 * 发送html格式的邮件
	 * @param subject  主题
	 * @param text  内容
	 * @param toAddresses   发送集合
	 * @param ccAddresses   抄送集合
	 * @param bccAddresses  密送集合
	 * @return
	 */
	public BaseResult sendHtmlMail(String subject,String text,String[] toAddresses,String[] ccAddresses,String[] bccAddresses);
	
	/**
	 * 发送html格式的邮件
	 * @param subject 主题
	 * @param html  内容
	 * @param toAddress 接收者的用户名
	 * @return
	 */
	public BaseResult sendHtmlMail(String subject,String html,String toAddress);
	
	
	/**
	 * 发送含有附件的邮件
	 * @param subject  主题
	 * @param text  内容
	 * @param toAddresses   发送集合
	 * @param ccAddresses   抄送集合
	 * @param bccAddresses  密送集合
	 * @param attachFile  附件全路径集合
	 * @return
	 */
	public BaseResult sendAttachMail(String subject,String text,String[] toAddresses,String[] ccAddresses,String[] bccAddresses,String[] attachFile);
	
	/**
	 * 发送含有附件的邮件
	 * @param subject  主题
	 * @param text  内容
	 * @param toAddress  接收者的用户名
	 * @param attachFile  附件全路径集合
	 * @return
	 */
	public BaseResult sendAttachMail(String subject,String text,String toAddress,String[] attachFile);
}
