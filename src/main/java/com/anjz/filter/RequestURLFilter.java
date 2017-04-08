package com.anjz.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.base.util.WebUtils;

/**
 * 获取请求地址的filter
 * @author ding.shuai
 * @date 2017年4月8日上午11:48:17
 */
public class RequestURLFilter implements Filter{

	private static final Logger logger =LoggerFactory.getLogger(RequestURLFilter.class);
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req =(HttpServletRequest)request;
		String currentUrl = WebUtils.getCurrentUrl(req);
		logger.info("当前请求地址(未解码):{}",currentUrl);
		logger.info("当前请求地址(解码后):{}",URLDecoder.decode(currentUrl, "utf-8"));	
	    logger.info("当前请求的编码格式：{},这个编码格式只对post请求有效！",request.getCharacterEncoding()); 
		
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
