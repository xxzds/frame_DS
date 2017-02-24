package com.anjz.core.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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

}
