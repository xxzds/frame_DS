package com.anjz.base.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author ding.shuai
 * @date 2016年8月23日下午3:11:09
 */
public class WebUtils {

	/**
	 * 获取当前urlRoot
	 * @param request
	 * @return
	 */
	public static String getLocaleUrlRoot(HttpServletRequest request) {
		// 获取当前url根路径，如 http://abc.aliyun-inc.net
		String curScheme = request.getScheme(); // http
		String curServerName = request.getServerName(); // abc.aliyun-inc.net
		int curServerPort = request.getServerPort();

		String curlocaleUrlRoot = curScheme + "://" + curServerName;
		if (curServerPort != 80) {
			curlocaleUrlRoot += ":" + curServerPort;
		}

		return curlocaleUrlRoot;
	}

	/**
	 * 获取主页url
	 */
	public static String getHomeUrl(HttpServletRequest request) {
		return getLocaleUrlRoot(request) + request.getContextPath();
	}

	/**
	 * 获取当前请求的地址
	 * @param request
	 * @return
	 */
	public static String getCurrentUrl(HttpServletRequest request) {
		// 根路径
		String localeUrl = getLocaleUrlRoot(request);

		String curUri = request.getRequestURI();
		//未进行解码，get请求参数
		String curQueryString = request.getQueryString();

		if (StringUtils.isNotBlank(curUri)) {
			localeUrl = localeUrl + curUri;
		}

		if (StringUtils.isNotBlank(curQueryString)) {
			localeUrl+="?"+curQueryString;
//			Map<String, String> queryPairs = splitQuery(curQueryString);
//			QueryStringBuilder queryStrBuilder = new QueryStringBuilder();
//			for (String key : queryPairs.keySet()) {
//				queryStrBuilder.addQueryParameter(key, queryPairs.get(key));
//			}
//
//			try {
//				localeUrl = localeUrl + queryStrBuilder.encode("UTF-8");
//			} catch (UnsupportedEncodingException e) {
//			}
		}

		return localeUrl;
	}

//	private static Map<String, String> splitQuery(String queryString) {
//		Map<String, String> queryPairs = new LinkedHashMap<String, String>();
//		try {
//			String[] pairs = queryString.split("&");
//			for (String pair : pairs) {
//				int idx = pair.indexOf("=");
//				queryPairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
//						URLDecoder.decode(pairn.substring(idx + 1), "UTF-8"));
//			}
//
//			return queryPairs;
//		} catch (Exception e) {
//			return queryPairs;
//		}
//	}
	
	
	/**
	 * 获取项目根的真实路径(清除末尾的斜杠，不同的web容器统一显示)
	 * @return
	 */
	public static String getRootRealPath(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String realPath= request.getSession().getServletContext().getRealPath("/");
        if(realPath.endsWith("/") || realPath.endsWith("\\")){
        	realPath=realPath.substring(0, realPath.length()-1);
        }
        return realPath;
	}
	
	
	/**
	 * 获取文件在项目中的真实路径
	 * @param relativePath   相对于项目的路径
	 * @return
	 */
	public static String getRealPath(String relativePath){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getSession().getServletContext().getRealPath(relativePath);
	}
	
	/**
	 * 获取项目名称
	 */
	public static String getContextPath(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getContextPath();
	}
	
	public static void main(String[] args) {
		String realPath ="a/b/";
		if(realPath.endsWith("/")){
        	realPath=realPath.substring(0, realPath.length()-1);
        }
		
		System.out.println(realPath);
		
	}

}
