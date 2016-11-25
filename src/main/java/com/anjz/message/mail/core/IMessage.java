package com.anjz.message.mail.core;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import com.anjz.message.mail.core.MailSenderInfo;

/**
 * 邮件内容接口
 * @author ding.shuai
 * @date 2016年7月28日下午4:00:18
 */
public interface IMessage {
	
	public MimeMessage createMail(Session session,MailSenderInfo mailInfo) throws Exception;

}
