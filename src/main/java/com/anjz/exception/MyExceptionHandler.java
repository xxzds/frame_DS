package com.anjz.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 自定义异常处理类
 * 
 * @author ding.shuai
 * @date 2016年8月14日上午10:42:53
 */
public class MyExceptionHandler implements HandlerExceptionResolver {
	
	private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView mv=new ModelAndView();
		mv.addObject("ex", ex);
		
		//授权失败
		if(ex instanceof UnauthorizedException){
			response.setStatus(org.springframework.http.HttpStatus.UNAUTHORIZED.value());
			mv.setViewName("unauthorized");
		}else{    //系统出现错误
			mv.setViewName("error");
		}
		
		//异常信息
		String errorMesage="";
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod =(HandlerMethod)handler;
			errorMesage="类名："+handlerMethod.getBean().getClass().getName()+",方法名："+handlerMethod.getMethod().getName();
		}		
		log.error(errorMesage, ex);

		return mv;
	}

}