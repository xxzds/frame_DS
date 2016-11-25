package com.anjz.shiro.filter;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import com.anjz.base.Constants;
import com.anjz.core.service.intf.system.SysUserService;

/**
 * 将当前用户的信息放入request中
 * 
 * @author ding.shuai
 * @date 2016年8月17日下午4:29:46
 */
public class SysUserFilter extends PathMatchingFilter {

	@Resource
	private SysUserService sysUserService;

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		String username = (String) SecurityUtils.getSubject().getPrincipal();
		request.setAttribute(Constants.CURRENT_USER, sysUserService.findByUsername(username).getData());

		return true;
	}
}
