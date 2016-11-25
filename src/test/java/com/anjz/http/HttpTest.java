package com.anjz.http;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.result.PlainResult;
import com.google.common.collect.Lists;

public class HttpTest extends BaseTest {

	@Resource
	private HttpCallService httpCallService;
	
	@Test
	public void testHttpGet(){
		httpCallService.httpGet("http://www.xh99d.com");
	}
	
	@Test
	public void testHttpPost(){
		httpCallService.httpPost("http://www.xh99d.com");
	}
	
	
	@Test
	public void testUrlConnectionGet(){
		PlainResult<String> result= httpCallService.urlConnectionGet("http://www.xh99d.com", null);
		logger.info(result.getData());
	}
	
	@Test
	public void testUrlConnectionPost(){
		PlainResult<String> result= httpCallService.urlConnectionPost("http://www.xh99d.com", null);
		logger.info(result.getData());
	}
	
	@Test
	public void test(){
		String params="[{\"a\":\"1\"},{\"b\":\"2\"}]";
		try {
//			HttpUrlConnectionUtil.doPost("http://localhost:8080/springmvc/request/ContentType", param, 5000);
			PlainResult<String> result=httpCallService.urlConnectionPost("http://127.0.0.1:8080/springmvc/request/ContentType", params);
			System.out.println("--------------------");
			logger.info(result.getData());
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	@Test
	public void test2(){
		List<File> lists=new ArrayList<File>();
		lists.add(new File("/Users/dingshuai/Desktop/logo.png"));
		httpCallService.httpUpload("http://127.0.0.1:8080/springmvc/file/upload", lists);
	}
}
