package com.anjz.core.controller.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.anjz.base.Constants;
import com.anjz.base.entity.response.AjaxUploadResponse;
import com.anjz.base.util.MessageUtils;
import com.anjz.result.PlainResult;
import com.anjz.upload.FileUploadService;
import com.anjz.util.AppUtil;
import com.anjz.util.ImagesUtils;

/**
 * 异步上传
 * 
 * @author ding.shuai
 * @date 2016年8月15日上午9:22:25
 */
@Controller
public class AjaxUploadController {
	
	private String[] IMAGE_EXTENSION = {
            "bmp", "gif", "jpg", "jpeg", "png"
    };

	@Resource
	private FileUploadService fileUploadService;

	/**
	 * 上传文件
	 * 
	 * @param files
	 * @return
	 */
	@RequestMapping(value="/ajaxUpload" , method = RequestMethod.POST)
	@ResponseBody
	public Object upload(HttpServletResponse response,
			@RequestParam(value="files" ,required = false) MultipartFile[] files) {
		
		//The file upload plugin makes use of an Iframe Transport module for browsers like Microsoft Internet Explorer and Opera, which do not yet support XMLHTTPRequest file uploads.
        response.setContentType("text/plain");
        
        AjaxUploadResponse ajaxUploadResponse=this.upload(files, null);
		return AppUtil.conversionJsonp(ajaxUploadResponse);
	}
	
	
	/**
	 * 只允许上传图片
	 * @param response
	 * @param files
	 * @return
	 */
	@RequestMapping(value="/ajaxUploadImage" , method = RequestMethod.POST)
	@ResponseBody
	public Object uploadImage(HttpServletResponse response,
			@RequestParam(value="files" ,required = false) MultipartFile[] files) {
		
		//The file upload plugin makes use of an Iframe Transport module for browsers like Microsoft Internet Explorer and Opera, which do not yet support XMLHTTPRequest file uploads.
        response.setContentType("text/plain");
        
        AjaxUploadResponse ajaxUploadResponse=this.upload(files, IMAGE_EXTENSION);
		return AppUtil.conversionJsonp(ajaxUploadResponse);
	}
	
	
	private AjaxUploadResponse upload(MultipartFile[] files, String[] allowedExtension) {
		AjaxUploadResponse ajaxUploadResponse = new AjaxUploadResponse();

		if (ArrayUtils.isEmpty(files)) {
			return ajaxUploadResponse;
		}

		for (MultipartFile file : files) {
			String filename = file.getOriginalFilename();
			long size = file.getSize();

			PlainResult<String> uploadResult = fileUploadService.uploadFile(file,allowedExtension);
			if (!uploadResult.isSuccess()) {
				ajaxUploadResponse.add(filename, size, uploadResult.getMessage());
				continue;
			}
			String url = uploadResult.getData();
			try {
				String deleteURL = "/ajaxUpload/delete?filename=" + URLEncoder.encode(url, Constants.UTF8);
				if (ImagesUtils.isImage(filename)) {
					ajaxUploadResponse.add(filename, size, url, url, deleteURL);
				} else {
					ajaxUploadResponse.add(filename, size, url, deleteURL);
				}
			} catch (UnsupportedEncodingException e) {
				ajaxUploadResponse.add(filename, size, MessageUtils.message("upload.server.error"));
				e.printStackTrace();
			}
		}
		return ajaxUploadResponse;
	}
	
	
	/**
	 *  删除文件
	 * @param filename  文件路径
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "ajaxUpload/delete", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxUploadDelete(@RequestParam(value = "filename") String filename) throws Exception {

        if (StringUtils.isEmpty(filename) || filename.contains("\\.\\.")) {
            return "";
        }
        filename = URLDecoder.decode(filename, Constants.UTF8);

        fileUploadService.delete(filename);
        return "";
    }
}
