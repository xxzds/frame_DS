package com.anjz.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
	
	@RequestMapping("/upload")
	public String test(){
		return "test/uploadFile";
	}

}
