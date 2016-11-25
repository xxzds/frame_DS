package com.anjz.core.controller.login;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.anjz.base.Constants;
import com.anjz.base.util.WebUtils;
import com.anjz.result.PlainResult;
import com.anjz.shiro.realm.DeletedAccountException;

/**
 * 登录
 * @author ding.shuai
 * @date 2016年8月17日下午6:01:46
 */
@Controller
public class LoginController {
	
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	@Value("${captcha.jcaptchaEbabled}")
	private Boolean jcaptchaEbabled;
	
	
	@RequestMapping(value="tologin")
	public String toLogin(HttpServletRequest request, Model model){
		model.addAttribute("jcaptchaEbabled", jcaptchaEbabled);
        
		//表示用户被管理员强制退出
        if (!StringUtils.isEmpty(request.getParameter("forcelogout"))) {
        	try {
        		Subject subject = SecurityUtils.getSubject();       		
        		if(subject != null && subject.getSession() != null && subject.isAuthenticated()){
        			subject.logout();
        		}        		
	        } catch (SessionException ise) {
	            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
	        }		      
            model.addAttribute(Constants.ERROR, "管理员已将您强制退出，如有问题，请联系管理员！");
        }
        model.addAttribute("currentUrl", WebUtils.getCurrentUrl(request));
		return "login/login";
	}
	
	
	@RequestMapping(value = "/login")
	@ResponseBody
    public PlainResult<String> showLoginForm(HttpServletRequest req, Model model) {
		PlainResult<String> result=new PlainResult<String>();
		
        String exceptionClassName = (String)req.getAttribute("shiroLoginFailure");
        if("jCaptcha.error".equals(exceptionClassName)) {
             result.setErrorMessage("验证码错误");
        } else if(exceptionClassName != null) {
        	result.setErrorMessage("其他错误：" + exceptionClassName);
        }else{
        	
        	String userName=req.getParameter("userName");
        	String password=req.getParameter("password");
        	if(StringUtils.isBlank(userName) || StringUtils.isBlank(password)){
        		result.setErrorMessage("用户名/密码错误");
        		return result;
        	}
        	
        	UsernamePasswordToken token=new UsernamePasswordToken(userName, password);        	
        	Subject subject= SecurityUtils.getSubject();       	
        	try{
        		subject.login(token);
        		//验证成功后跳转的地址        		
        		result.setData(WebUtils.getHomeUrl(req));
        	}catch(LockedAccountException e){
        		result.setErrorMessage("账户已锁定");
        		return result;
        	}catch(DeletedAccountException e){
        		result.setErrorMessage("账户已删除");
        		return result;
        	}catch(ExcessiveAttemptsException e){
        		result.setErrorMessage("登录密码错误5次，请10分钟以后再操作");
        		return result;
        	}
        	catch(AuthenticationException e){
        		result.setErrorMessage("用户名/密码错误");
        		return result;
        	}	
        }
               
        return result;
    }
}
