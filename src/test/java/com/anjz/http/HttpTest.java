package com.anjz.http;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;

import com.anjz.BaseTest;
import com.anjz.result.PlainResult;
import com.google.common.collect.Maps;
/**
 * @author ding.shuai
 * @date 2017年4月9日下午4:53:02
 */
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
	
	@Test
	public void test3() throws UnsupportedEncodingException{
		Map<String,String> params = Maps.newHashMap();
		params.put("name", "你好");
//		httpCallService.httpGet("http://localhost:9999/kj/test/ajax1", params);
		httpCallService.httpPost("http://localhost:9999/kj/test/ajax1", params);
	}
	
	@Test
	public void test4(){
		Map<String,String> params = Maps.newHashMap();
//		params.put("username", "18755123314");
//    	params.put("password", "nxl1232104125");
    	httpCallService.httpPost("http://www.dataoke.com/login", params);
//    	httpCallService.urlConnectionPost("http://www.dataoke.com/loginApi",null);
    	
	}
}
