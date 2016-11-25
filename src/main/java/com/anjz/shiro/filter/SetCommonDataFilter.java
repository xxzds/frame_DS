package com.anjz.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;

import com.anjz.base.Constants;

/**
 * 设置公共数据
 * 
 * @author ding.shuai
 * @date 2016年8月23日下午3:35:48
 */
public class SetCommonDataFilter extends PathMatchingFilter {

	@Override
	protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue)
			throws Exception {
		HttpServletRequest req = (HttpServletRequest) request;
		// ctx---->request.contextPath
		if (request.getAttribute(Constants.CONTEXT_PATH) == null) {			
			request.setAttribute(Constants.CONTEXT_PATH, req.getContextPath());
		}
		
		//存入上个页面地址
		if (request.getAttribute(Constants.BACK_URL) == null) {
            request.setAttribute(Constants.BACK_URL, extractBackURL(req));
        }
		return true;
	}
	
	
	
	 /**
     * 上一次请求的地址(重定向的地址不能获取)
     * 1、先从request.parameter中查找BackURL
     * 2、获取header中的 referer
	 * @param request
	 * @return
	 */
	 private String extractBackURL(HttpServletRequest request) {
		 String url=request.getParameter(Constants.BACK_URL);
		 
		 if (StringUtils.isEmpty(url)) {
	            url = request.getHeader("Referer");
	        }

	        if(!StringUtils.isEmpty(url) && (url.startsWith("http://") || url.startsWith("https://"))) {
	            return url;
	        }

	        if (!StringUtils.isEmpty(url) && url.startsWith(request.getContextPath())) {
	            url = getBasePath(request) + url;
	        }
	        return url;
	 }
	 
	 private String getBasePath(HttpServletRequest req) {
	        StringBuffer baseUrl = new StringBuffer();
	        String scheme = req.getScheme();
	        int port = req.getServerPort();

	        //String		servletPath = req.getServletPath ();
	        //String		pathInfo = req.getPathInfo ();

	        baseUrl.append(scheme);        // http, https
	        baseUrl.append("://");
	        baseUrl.append(req.getServerName());
	        if ((scheme.equals("http") && port != 80) || (scheme.equals("https") && port != 443)) {
	            baseUrl.append(':');
	            baseUrl.append(req.getServerPort());
	        }
	        return baseUrl.toString();
	    }
}
