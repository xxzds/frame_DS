package com.anjz.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anjz.core.model.SysAuth;
import com.anjz.core.service.intf.test.TestService;

@Controller
@RequestMapping("/test")
public class TestController {
	
	@Resource
	private TestService testService;
	
	@RequestMapping("/upload")
	public String test(){
		return "test/uploadFile";
	}
	
	
	
	@RequestMapping("/testException")
	public String testException(){
		testService.testException();		
		return "/test/uploadFile";
	}
	
	
	@RequestMapping("/ajax1")
	@ResponseBody
	public String ajax1(){
		return "aaa";
	}
	
	@RequestMapping("/ajax2")
	@ResponseBody
	public SysAuth ajax2(){
		SysAuth sysAuth = new SysAuth();
		return sysAuth;
		
	}

}
