package com.anjz.message.mail.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.result.BaseResult;
import com.anjz.result.CommonResultCode; 

/**
 * 发送邮件工具类
 * @author ding.shuai
 * @date 2016年7月28日下午4:08:15
 */
public class MailSenderUtil  {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailSenderUtil.class);
	
	private static BaseResult sendMail(MailSenderInfo mailInfo,IMessage message) {	  
		Properties prop = mailInfo.getProperties();
		// 1、创建session
		Session session = Session.getInstance(prop);
		// 开启Session的debug模式，这样就可以查看到程序发送Email的运行状态
		session.setDebug(true);

		try {
			// 2、通过session得到transport对象
			Transport ts = session.getTransport();
			// 3、连上邮件服务器，需要发件人提供邮箱的用户名和密码进行验证
			ts.connect(mailInfo.getMailServerHost(), mailInfo.getUserName(), mailInfo.getPassword());
			// 4、创建邮件
			Message mailMessage = message.createMail(session, mailInfo);
			// 5、发送邮件
			ts.sendMessage(mailMessage, mailMessage.getAllRecipients());
			ts.close();
			return new BaseResult();
		} catch (Exception e) {
			LOGGER.error("exception",e);
			String msg = e.getCause() == null ? e.toString() : e.getCause().getMessage();
			return new BaseResult().setErrorMessage(CommonResultCode.EXCEPITON_HTTP_CALL, msg);
		}
	}	
	
	
	
	/**
	 * 发送普通文本邮件
	 * @param mailInfo
	 * @return
	 */
	public static BaseResult sendTextMail(MailSenderInfo mailInfo){
		return sendMail(mailInfo, new IMessage() {			
			@Override
			public MimeMessage createMail(Session session, MailSenderInfo mailInfo) throws Exception {
				// 创建邮件对象
				MimeMessage message = new MimeMessage(session);
				// 邮件的发件人
				InternetAddress fromAdress=null;
				String addressAlias=mailInfo.getFromAddressAlias();
				if(StringUtils.isEmpty(addressAlias)){
					fromAdress=new InternetAddress(mailInfo.getFromAddress()); 
				}else{
					fromAdress=new InternetAddress(mailInfo.getFromAddress(),addressAlias);
				}
				message.setFrom(fromAdress);
				
				// 邮件收件人
				String[] toAddresses= mailInfo.getToAddresses();
				if(toAddresses!=null){   //发送
					InternetAddress[] toList = InternetAddress.parse(StringUtils.join(toAddresses,",")); 
					message.setRecipients(Message.RecipientType.TO, toList);
				}
				
				String[] ccAddresses=mailInfo.getCcAddresses();
				if(ccAddresses!=null){  //抄送
					InternetAddress[] ccList = InternetAddress.parse(StringUtils.join(ccAddresses,",")); 
					message.setRecipients(Message.RecipientType.CC, ccList);
				}
				
				String[] bccAddresses=mailInfo.getBccAddresses();
				if(bccAddresses!=null){  //密送
					InternetAddress[] bccList = InternetAddress.parse(StringUtils.join(bccAddresses,",")); 
					message.setRecipients(Message.RecipientType.BCC, bccList);
				}
				
				// 设置邮件消息的主题
				message.setSubject(mailInfo.getSubject());
				// 设置邮件消息发送的时间
				message.setSentDate(new Date());
				// 设置邮件消息的主要内容
				message.setText(mailInfo.getContent());
				// 返回创建好的邮件对象
				return message;
			}
		});
	}
	
	/**
	 * 发送html格式邮件
	 * @param mailInfo
	 * @return
	 */
	public static BaseResult  sendHtmlMail(MailSenderInfo mailInfo){
		return sendMail(mailInfo, new IMessage() {			
			@Override
			public MimeMessage createMail(Session session, MailSenderInfo mailInfo) throws Exception {
				// 创建邮件对象
				MimeMessage message = new MimeMessage(session);
				// 邮件的发件人
				InternetAddress fromAdress=null;
				String addressAlias=mailInfo.getFromAddressAlias();
				if(StringUtils.isEmpty(addressAlias)){
					fromAdress=new InternetAddress(mailInfo.getFromAddress()); 
				}else{
					fromAdress=new InternetAddress(mailInfo.getFromAddress(),addressAlias);
				}
				message.setFrom(fromAdress);
				// 邮件收件人
				String[] toAddresses= mailInfo.getToAddresses();
				if(toAddresses!=null){   //发送
					InternetAddress[] toList = InternetAddress.parse(StringUtils.join(toAddresses,",")); 
					message.setRecipients(Message.RecipientType.TO, toList);
				}
				
				String[] ccAddresses=mailInfo.getCcAddresses();
				if(ccAddresses!=null){  //抄送
					InternetAddress[] ccList = InternetAddress.parse(StringUtils.join(ccAddresses,",")); 
					message.setRecipients(Message.RecipientType.CC, ccList);
				}
				
				String[] bccAddresses=mailInfo.getBccAddresses();
				if(bccAddresses!=null){  //密送
					InternetAddress[] bccList = InternetAddress.parse(StringUtils.join(bccAddresses,",")); 
					message.setRecipients(Message.RecipientType.BCC, bccList);
				}
				// 设置邮件消息的主题
				message.setSubject(mailInfo.getSubject());
				// 设置邮件消息发送的时间
				message.setSentDate(new Date());
		        
		        //设置内容
		        MimeBodyPart html = new MimeBodyPart();
		        html.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
		        // 描述数据关系
		        MimeMultipart mainPart = new MimeMultipart();
		        mainPart.addBodyPart(html);

		        message.setContent(mainPart);
		        message.saveChanges();
		        
				// 返回创建好的邮件对象
				return message;
			}
		});
	}
	
	
	/**
	 * 发送含有附件的邮件
	 * @param mailInfo
	 * @return
	 */
	public static BaseResult sendAttachMail(MailSenderInfo mailInfo){
		return sendMail(mailInfo, new IMessage() {			
			@Override
			public MimeMessage createMail(Session session, MailSenderInfo mailInfo) throws Exception {
				// 创建邮件对象
				MimeMessage message = new MimeMessage(session);
				// 邮件的发件人
				InternetAddress fromAdress=null;
				String addressAlias=mailInfo.getFromAddressAlias();
				if(StringUtils.isEmpty(addressAlias)){
					fromAdress=new InternetAddress(mailInfo.getFromAddress()); 
				}else{
					fromAdress=new InternetAddress(mailInfo.getFromAddress(),addressAlias);
				}
				message.setFrom(fromAdress);
				// 邮件收件人
				String[] toAddresses= mailInfo.getToAddresses();
				if(toAddresses!=null){   //发送
					InternetAddress[] toList = InternetAddress.parse(StringUtils.join(toAddresses,",")); 
					message.setRecipients(Message.RecipientType.TO, toList);
				}
				
				String[] ccAddresses=mailInfo.getCcAddresses();
				if(ccAddresses!=null){  //抄送
					InternetAddress[] ccList = InternetAddress.parse(StringUtils.join(ccAddresses,",")); 
					message.setRecipients(Message.RecipientType.CC, ccList);
				}
				
				String[] bccAddresses=mailInfo.getBccAddresses();
				if(bccAddresses!=null){  //密送
					InternetAddress[] bccList = InternetAddress.parse(StringUtils.join(bccAddresses,",")); 
					message.setRecipients(Message.RecipientType.BCC, bccList);
				}
				// 设置邮件消息的主题
				message.setSubject(mailInfo.getSubject());
				// 设置邮件消息发送的时间
				message.setSentDate(new Date());
		        
		        //设置内容
		        MimeBodyPart text = new MimeBodyPart();
		        text.setContent(mailInfo.getContent(), "text/html;charset=UTF-8");
		        
		        //创建邮件附件
		        List<MimeBodyPart> attaches=new ArrayList<MimeBodyPart>();
		        String[] attachFileNames=mailInfo.getAttachFileNames();
		        if (attachFileNames!=null) {
					for(String attachFileName:attachFileNames){
						MimeBodyPart attach = new MimeBodyPart();
				        DataHandler dh = new DataHandler(new FileDataSource(attachFileName));
				        attach.setDataHandler(dh);
				        attach.setFileName(MimeUtility.encodeText(dh.getName()));
				        attaches.add(attach);
					}
				}
		       
		        // 描述数据关系
		        MimeMultipart mainPart = new MimeMultipart();
		        mainPart.addBodyPart(text);
		        for(MimeBodyPart attach:attaches){
		        	mainPart.addBodyPart(attach);
		        }
		        mainPart.setSubType("mixed");

		        message.setContent(mainPart);
		        message.saveChanges();
		        
				// 返回创建好的邮件对象
				return message;
			}
		});
	}
} 