package com.anjz.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 读取properties文件的工具类
 * @author ding.shuai
 * @date 2016年7月28日下午9:06:56
 */
public class PropertiesUtil {
	
	private Properties properties=new Properties();
	
	private PropertiesUtil(){		
	}
	
	private PropertiesUtil(String path){
		try{
			String pathString = PropertiesUtil.class.getClassLoader().getResource(path).getFile();
			if("/".equals(File.separator)){   
				pathString = pathString.replace("\\",File.separator);
			}
			pathString =  pathString.replace("%20"," ");
			FileInputStream inputStream=new FileInputStream(pathString);
			properties.load(inputStream);
		}catch(IOException e){
			e.printStackTrace();
		}
		
	}
	
	public static PropertiesUtil getInstance(String path){
		return new PropertiesUtil(path);
	}
	
	
	public String getValue(String key){		
		String value=null;
		try {
			//解决中文乱码问题
			 value = new String(properties.getProperty(key).getBytes("ISO-8859-1"), "UTF-8");		
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		if (value == null) {
			return "";
		}
		return value;
	}
	
	public static void main(String[] args) {
		System.out.println(PropertiesUtil.getInstance("mail.properties").getValue("mail.from.alias"));
	}
	
}
