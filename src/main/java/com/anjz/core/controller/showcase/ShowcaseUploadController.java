package com.anjz.core.controller.showcase;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.controller.BaseCRUDController;
import com.anjz.core.model.ShowcaseUpload;
import com.anjz.result.PlainResult;
import com.anjz.upload.FileUploadService;

/**
 * 实例管理  -> 文件上传列表
 * @author ding.shuai
 * @date 2016年9月20日下午10:44:07
 */
@Controller
@RequestMapping("/showcase/upload")
public class ShowcaseUploadController extends BaseCRUDController<ShowcaseUpload, String>{

	@Resource
	private FileUploadService fileUploadService;
	
	public ShowcaseUploadController() {
		setResourceIdentity("showcase:upload");
	}
	
	@RequestMapping(value = "create/discard", method = RequestMethod.POST)
	@Override
	public String create(HttpServletRequest request, Model model, @Valid @ModelAttribute("m") ShowcaseUpload m, BindingResult result,
			RedirectAttributes redirectAttributes) {
		throw new RuntimeException("discarded method");
	}
	
		
	/**
	 * 普通新增
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public String create(HttpServletRequest request,
			@RequestParam(value = "file", required = false) MultipartFile file,
            Model model,  @Valid @ModelAttribute("m") ShowcaseUpload m, BindingResult result,
            RedirectAttributes redirectAttributes) {
		if (!file.isEmpty()) {
            PlainResult<String> uploadResult = fileUploadService.uploadFile(file, null);
			if(!uploadResult.isSuccess()){
				result.reject(null,uploadResult.getMessage());
			}else{
				m.setSrc(uploadResult.getData());
			}		
        }
		return super.create(request, model, m, result, redirectAttributes);
	}
	
	@RequestMapping(value = "{id}/update/discard", method = RequestMethod.POST)
    @Override
    public String update(
    		Model model, 
    		@Valid @ModelAttribute("m") ShowcaseUpload upload, BindingResult result, 
    		@RequestParam(value = "BackURL", required = false) String backURL, RedirectAttributes redirectAttributes) {
        throw new RuntimeException("discarded method");
    }

    @RequestMapping(value = "{id}/update", method = RequestMethod.POST)
    public String update(
    		@PathVariable("id") String id,
            Model model,
            HttpServletRequest request, @RequestParam(value = "file", required = false) MultipartFile file,
            @Valid @ModelAttribute("m") ShowcaseUpload m, BindingResult result,
            @RequestParam(value = "BackURL") String backURL,
            RedirectAttributes redirectAttributes) {

    	if (!file.isEmpty()) {
            PlainResult<String> uploadResult = fileUploadService.uploadFile(file, null);
			if(!uploadResult.isSuccess()){
				result.reject(null,uploadResult.getMessage());
			}else{
				m.setSrc(uploadResult.getData());
			}		
        }
        return super.update(model, m, result, backURL, redirectAttributes);
    }
	
	
}