package com.anjz.message.mail;

import com.anjz.message.mail.core.MailSenderInfo;
import com.anjz.message.mail.core.MailSenderUtil;
import com.anjz.result.BaseResult;

/**
 * @author ding.shuai
 * @date 2016年7月29日下午3:43:06
 */
public class MailSendServiceImpl implements MailSendService{
	
	private  MailSenderInfo mailInfo;
	
	public MailSenderInfo getMailInfo() {
		return mailInfo;
	}

	public void setMailInfo(MailSenderInfo mailInfo) {
		this.mailInfo = mailInfo;
	}
	

	@Override
	public BaseResult sendTextMail(String subject,String text,String toAddress) {
		return sendTextMail(subject, text, new String[]{toAddress}, null, null);
	}
	
	@Override
	public BaseResult sendTextMail(String subject, String text, String[] toAddresses, String[] ccAddresses,
			String[] bccAddresses) {
		mailInfo.setSubject(subject);
		mailInfo.setContent(text);
		mailInfo.setToAddresses(toAddresses);
		mailInfo.setCcAddresses(ccAddresses);
		mailInfo.setBccAddresses(bccAddresses);
		return MailSenderUtil.sendTextMail(mailInfo);
	}
	
	
	@Override
	public BaseResult sendHtmlMail(String subject, String html, String toAddress) {
		return sendHtmlMail(subject, html, new String[]{toAddress}, null, null);
	}
	
	@Override
	public BaseResult sendHtmlMail(String subject, String text, String[] toAddresses, String[] ccAddresses,
			String[] bccAddresses) {
		mailInfo.setSubject(subject);
		mailInfo.setContent(text);
		mailInfo.setToAddresses(toAddresses);
		mailInfo.setCcAddresses(ccAddresses);
		mailInfo.setBccAddresses(bccAddresses);
		return MailSenderUtil.sendHtmlMail(mailInfo);
	}

	@Override
	public BaseResult sendAttachMail(String subject, String text, String toAddress, String[] attachFile) {
		return sendAttachMail(subject, text, new String[]{toAddress}, null,null, attachFile);
	}

	@Override
	public BaseResult sendAttachMail(String subject, String text, String[] toAddresses, String[] ccAddresses,
			String[] bccAddresses,String[] attachFile) {
		mailInfo.setSubject(subject);
		mailInfo.setContent(text);
		mailInfo.setToAddresses(toAddresses);
		mailInfo.setCcAddresses(ccAddresses);
		mailInfo.setBccAddresses(bccAddresses);
		mailInfo.setAttachFileNames(attachFile);
		return MailSenderUtil.sendAttachMail(mailInfo);
	}
}
