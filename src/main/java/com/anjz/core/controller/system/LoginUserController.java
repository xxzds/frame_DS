package com.anjz.core.controller.system;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anjz.base.Constants;
import com.anjz.base.bind.annotation.CurrentUser;
import com.anjz.base.controller.BaseController;
import com.anjz.core.enums.UserStatus;
import com.anjz.core.model.SysUser;
import com.anjz.core.model.SysUserOrganizationJob;
import com.anjz.core.service.intf.system.PasswordHelperService;
import com.anjz.core.service.intf.system.SysUserOrganizationJobService;
import com.anjz.core.service.intf.system.SysUserService;
import com.anjz.result.BaseResult;

/**
 * 登录用户的个人信息
 * @author ding.shuai
 * @date 2016年9月6日上午8:14:03
 */
@Controller
@RequestMapping("system/user/loginUser")
public class LoginUserController extends BaseController<SysUser, String>{

	@Resource
	private SysUserService sysUserService;
	
	@Resource
	private SysUserOrganizationJobService sysUserOrganizationJobService;
	
	@Resource
	private PasswordHelperService passwordHelperService;
	
	@Override
    public void setCommonData(Model model) {
        model.addAttribute("statusList", UserStatus.values());
    }
	
	/**
	 * 查看个人资料
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping("/viewInfo")
    public String viewInfo(@CurrentUser SysUser user, Model model) {
        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "查看个人资料");
        
        user = sysUserService.findOne(user.getId()).getData();
        model.addAttribute("user", user);
        
        //组织机构和职务
        List<SysUserOrganizationJob> userOrganizationJobs= sysUserOrganizationJobService.findOrganizationJobByUserId(user.getId()).getData();		
		model.addAttribute("organizationJobsMap", sysUserOrganizationJobService.getDisplayOrganizationJobs(userOrganizationJobs));
		
        return viewName("editForm");
    }
	
	
	/**
	 * 修改个人资料
	 * @param user
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/updateInfo", method = RequestMethod.GET)
    public String updateInfoForm(@CurrentUser SysUser user, Model model) {
        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "修改个人资料");
        model.addAttribute("user", user);
        return viewName("editForm");
    }
	
	@RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public String updateInfo(
            @CurrentUser SysUser user,
            @RequestParam("userEmail") String userEmail,
            @RequestParam("userPhone") String userPhone,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (userEmail == null || !userEmail.matches(SysUser.EMAIL_PATTERN)) {
            model.addAttribute(Constants.ERROR, "请输入正确的邮箱地址");
            return updateInfoForm(user, model);
        }

        if (userPhone == null || !userPhone.matches(SysUser.MOBILE_PHONE_NUMBER_PATTERN)) {
            model.addAttribute(Constants.ERROR, "请输入正确的手机号");
            return updateInfoForm(user, model);
        }

        SysUser emailDbUser = sysUserService.findByUserEmail(userEmail).getData();
        if (emailDbUser != null && !emailDbUser.equals(user)) {
            model.addAttribute(Constants.ERROR, "邮箱地址已经被其他人使用，请换一个");
            return updateInfoForm(user, model);
        }

        SysUser mobilePhoneNumberDbUser = sysUserService.findByUserPhone(userPhone).getData();
        if (mobilePhoneNumberDbUser != null && !mobilePhoneNumberDbUser.equals(user)) {
            model.addAttribute(Constants.ERROR, "手机号已经被其他人使用，请换一个");
            return updateInfoForm(user, model);
        }

        user.setUserEmail(userEmail);
        user.setUserPhone(userPhone);
        BaseResult baseResult= sysUserService.updateSelectiveById(user);
        if(!baseResult.isSuccess()){
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改个人资料成功");
        }
        return redirectToUrl(viewName("updateInfo"));
    }
	
	/**
	 * 修改密码
	 */
	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
    public String changePasswordForm(@CurrentUser SysUser user, Model model) {
        setCommonData(model);
        model.addAttribute(Constants.OP_NAME, "修改密码");
        model.addAttribute("user", user);
        return viewName("changePasswordForm");
    }
	
	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public String changePassword(
            @CurrentUser SysUser user,
            @RequestParam(value = "oldPassword") String oldPassword,
            @RequestParam(value = "newPassword1") String newPassword1,
            @RequestParam(value = "newPassword2") String newPassword2,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!passwordHelperService.matches(user, oldPassword)) {
            model.addAttribute(Constants.ERROR, "旧密码不正确");
            return changePasswordForm(user, model);
        }

        if (StringUtils.isEmpty(newPassword1) || StringUtils.isEmpty(newPassword2)) {
            model.addAttribute(Constants.ERROR, "必须输入新密码");
            return changePasswordForm(user, model);
        }

        if (!newPassword1.equals(newPassword2)) {
            model.addAttribute(Constants.ERROR, "两次输入的密码不一致");
            return changePasswordForm(user, model);
        }

        BaseResult baseResult = sysUserService.changePassword(new String[]{user.getId()}, newPassword1);
        if(!baseResult.isSuccess()){
        	 redirectAttributes.addFlashAttribute(Constants.MESSAGE, baseResult.getMessage());
        }else{
        	redirectAttributes.addFlashAttribute(Constants.MESSAGE, "修改密码成功");
        }
        return redirectToUrl(viewName("changePassword"));
    }
}
