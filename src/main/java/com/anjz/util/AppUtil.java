package com.anjz.util;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.util.JSONPObject;

/**
 * 
 * @author ding.shuai
 * @date 2016年8月15日上午9:47:02
 */
public class AppUtil {
	
	/**
	 * 判断json字符串是否需要转化成jsonp格式
	 * @param request
	 * @param result
	 * @return
	 */
	public static Object conversionJsonp(Object result){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return conversionJsonp(request, result);
	}
	

	public static Object conversionJsonp(HttpServletRequest request,Object result){
		Map<?, ?> paramsMap= request.getParameterMap();
		if(paramsMap.containsKey("callback")){
			String callback=(String)paramsMap.get("callback");
			return new JSONPObject(callback, result);
		}
		return result;
	}
}
