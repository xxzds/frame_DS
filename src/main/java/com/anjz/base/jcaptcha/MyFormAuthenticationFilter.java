package com.anjz.base.jcaptcha;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 用于验证码验证的 Shiro 拦截器在用于身份认证的拦截器之前运行;但是如果验证码验证 拦截器失败了,就不需要进行身份认证拦截器流程了
 * @author ding.shuai
 * @date 2016年8月23日上午10:10:17
 */
public class MyFormAuthenticationFilter extends FormAuthenticationFilter {
	
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if(request.getAttribute(getFailureKeyAttribute()) != null) {
            return true;
        }
		return super.isAccessAllowed(request, response, mappedValue);
	}
}
