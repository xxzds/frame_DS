package com.anjz.mail;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.http.HttpCallService;
import com.anjz.message.mail.MailSendService;

public class mailTest extends BaseTest {
	
	@Resource
	private MailSendService mailSendService;
	
	@Resource
	private HttpCallService httpCallService;

	@Test
	public void textMailTest(){
//		mailSendService.sendTextMail("测试","<div><span style='color:red;'>666</span></div>","1114332905@qq.com");
		mailSendService.sendTextMail("发给多人","<div>您好，本次操作您的验证码为：<span style='color:red;'>234234</span></div>",new String[]{"1114332905@qq.com","yanghaoyi080512@sina.com"},null,null);
		
	}
	
	@Test
	public void htmlMailTest(){		
//		String html="<div><span style='color:red;'>666<img src='https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=505742766,1788429344&fm=116&gp=0.jpg'/></span></div>";
		String html="<div><span style='color:red;'>您好，本次操作您的验证码为:343434</span></div>";
		mailSendService.sendHtmlMail("测试html格式的邮件", html, "1114332905@qq.com");
	}
	
	@Test
	public void attachMailTest(){
		String html="<div><a href='http://www.baidu.com'>百度</a></div>";
		String[] attaches={"/Users/dingshuai/Desktop/z_payway.sql",
							"/Users/dingshuai/Desktop/测试.xml"};
		mailSendService.sendAttachMail("测试含有附件的邮件", html, "1114332905@qq.com", attaches);
	}
}
