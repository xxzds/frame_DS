package com.anjz.core.controller.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 跳转错误的提示页面
 * @author ding.shuai
 * @date 2016年9月19日下午9:34:22
 */
@Controller
public class PromptController {

	@RequestMapping("/repeatsubmit")
	public String repeatSubmit(){
		return "repeatsubmit";
	}
}
