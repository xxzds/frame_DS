package com.anjz.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.anjz.upload.FileUploadToLocalServiceImpl;

/**
 * 路径工具类
 * 
 * @author ding.shuai
 * @date 2016年8月17日下午12:56:30
 */
public class PathUtil {
	private static final Logger log = LoggerFactory.getLogger(FileUploadToLocalServiceImpl.class);

	private static String classPath = PathUtil.class.getClassLoader().getResource("/").getPath();

	/**
	 * 获取web项目的根路径
	 * 
	 * @return
	 */
	public static String getRootPath() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		return request.getSession().getServletContext().getRealPath("/");
	}

//	/**
//	 * 获取web项目的根路径
//	 * @return
//	 */
//	public static String getRootPath() {
//		String rootPath = "";
//		// windows下
//		if ("\\".equals(File.separator)) {
//			log.info("windows");
//			rootPath = classPath + "..\\..\\";
//			rootPath = rootPath.replace("/", "\\");
//		}
//		// linux下
//		if ("/".equals(File.separator)) {
//			log.info("linux");
//			rootPath = classPath + "../../";
//			rootPath = rootPath.replace("\\", "/");
//		}
//		return rootPath;
//	}
}
