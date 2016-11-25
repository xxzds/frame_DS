package com.anjz.core.controller.showcase;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.controller.BaseController;
import com.anjz.core.model.ShowcaseUpload;
import com.anjz.core.service.intf.showcase.ShowaseUploadService;
import com.anjz.result.BaseResult;

/**
 * ajax 新增
 * @author ding.shuai
 * @date 2016年9月22日上午10:58:51
 */
@Controller
@RequestMapping(value = "showcase/upload/ajax")
public class AjaxUploadFormController extends BaseController<ShowcaseUpload, String>{

    @Resource
    private ShowaseUploadService uploadService;

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public String showCreateForm(Model model) {
        model.addAttribute(Constants.OP_NAME, "新增");
        if (!model.containsAttribute("upload")) {
            model.addAttribute("upload", newModel());
        }
        return viewName("editForm");
    }

    @RequiresPermissions("showcase:upload:create")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid ShowcaseUpload upload, RedirectAttributes redirectAttributes) {

        BaseResult result= uploadService.saveSelective(upload);
        if(!result.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.ERROR, result.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "创建文件成功");
        }        
        return redirectToUrl("/showcase/upload");
    }
}
