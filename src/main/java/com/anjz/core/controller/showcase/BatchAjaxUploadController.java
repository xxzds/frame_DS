package com.anjz.core.controller.showcase;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.anjz.base.Constants;

/**
 * ajax批量上传
 * 
 * @author ding.shuai
 * @date 2016年9月22日上午10:59:16
 */
@Controller
public class BatchAjaxUploadController {

	@RequiresPermissions("showcase:upload:create")
	@RequestMapping(value = "ajaxUpload", method = RequestMethod.GET)
	public String showAjaxUploadForm(Model model) {
		model.addAttribute(Constants.OP_NAME, "新增");
		return "showcase/upload/ajax/uploadForm";
	}

}
