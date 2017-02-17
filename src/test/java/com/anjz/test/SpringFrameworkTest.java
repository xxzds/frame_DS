package com.anjz.test;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;

import com.anjz.BaseTest;

/**
 * 
 * @author shuai.ding
 *
 * @date 2017年2月17日下午3:04:09
 */
public class SpringFrameworkTest extends BaseTest{
	
	@Autowired
	private PropertiesFactoryBean configProperties;
	
	/**
	 * 读取配置文件
	 * @throws IOException
	 */
	@Test
	public void readConfigTest() throws IOException{
		Properties properties = configProperties.getObject();
		
		String mailFromAlias = properties.getProperty("mail.from.alias");
		String fileSrc = properties.getProperty("icon.css.file.src");
		
		logger.info("邮件发送者的别名：{}",mailFromAlias);
		logger.info("图标的文件位置：{}",fileSrc);
	}
}
