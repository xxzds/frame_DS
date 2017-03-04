package com.anjz.upload;

import org.apache.commons.fileupload.FileItem;

import com.anjz.result.PlainResult;

/**
 * 文件上传服务（底层抽象）
 * 
 * @author ding.shuai
 * @date 2016年8月17日上午9:08:37
 */
public interface FileUploadService2 {
	
	/**
	 * 上传文件，默认生成文件名
	 * @param fileItem   文件
	 * @return
	 */
	PlainResult<String> uploadFile(FileItem fileItem);

	/**
	 * 上传文件，自定义文件名
	 * @param fileItem  文件
	 * @param destFileName 存储的文件名，如a.jpg
	 * @return
	 */
	PlainResult<String> uploadFile(FileItem fileItem, String destFileName);
}
