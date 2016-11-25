package com.anjz.template;

import java.io.StringWriter;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.core.model.MaintainDictionary;
import com.anjz.core.model.MaintainTemplate;
import com.anjz.core.service.intf.maintain.MaintainDictionaryService;
import com.anjz.core.service.intf.maintain.MaintainTemplateService;
import com.anjz.message.mail.MailSendService;
import com.google.common.collect.Maps;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 模板测试
 * @author ding.shuai
 * @date 2016年9月14日下午7:51:38
 */
public class TemplateTest extends BaseTest{

	@Resource
	private MaintainDictionaryService dictionaryService;
	
	@Resource
	private MaintainTemplateService templateService;
	
	@Resource
	private MailSendService mailSendService;
	
	@Test
	public void senderEmailCodeTest(){
		MaintainDictionary dictionary= dictionaryService.findDictinaryByCode("YJYZMMB").getData();
		
		MaintainTemplate templatex=new MaintainTemplate();
		templatex.setType(dictionary.getId());
		templatex=templateService.find(templatex).getData().get(0);
		
		System.out.println(templatex.getTemplate());
		
		try {  
		     Configuration cfg = new Configuration();      
		     StringTemplateLoader stl =  new StringTemplateLoader();  
		     stl.putTemplate("", templatex.getTemplate());  
		     cfg.setTemplateLoader(stl);      
		     Template template = cfg.getTemplate("");  
		       
		     Map<String,Object> dataModel=Maps.newHashMap();
		     dataModel.put("code", "234234");
		       
		     StringWriter writer = new StringWriter();      
		     template.process(dataModel, writer);      
		     String templateString= writer.toString();  
		     System.out.println(templateString);
		     
		     
		     mailSendService.sendHtmlMail("邮件验证码", templateString, "1114332905@qq.com");
		     
		     
		 } catch (Exception e) {  
		     // TODO Auto-generated catch block  
		     e.printStackTrace();  
		 }      
		
		
	}
}
