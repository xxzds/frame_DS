package com.anjz.upload;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.result.CommonResultCode;
import com.anjz.result.PlainResult;
import com.anjz.util.PathUtil;
import com.anjz.util.UuidUtil;

/**
 * 上传到本地
 * 
 * @author ding.shuai
 * @date 2016年8月17日上午9:09:53
 */
public class FileUploadToLocal2ServiceImpl implements FileUploadService2 {
	private static final Logger log = LoggerFactory.getLogger(FileUploadToLocal2ServiceImpl.class);

	// 默认上传的地址
	private static String defaultBaseDir;
	
	public static String getDefaultBaseDir() {
		return defaultBaseDir;
	}

	public static void setDefaultBaseDir(String defaultBaseDir) {
		FileUploadToLocal2ServiceImpl.defaultBaseDir = defaultBaseDir;
	}

	private String getFileType(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            fileName = fileName.trim();

            int dotInd = fileName.lastIndexOf('.');
            if (dotInd >= 0) {
                String result = fileName.substring(dotInd);
                return result.replace('?', '_').replace('#', '_');
            }
        }
        return StringUtils.EMPTY;
    }
	
	private String generateFileName(String fileName) {
        return UuidUtil.generateUuid36() + getFileType(fileName);
    }

	@Override
	public PlainResult<String> uploadFile(FileItem fileItem) {
		return uploadFile(fileItem, generateFileName(fileItem.getName()));
	}

	@Override
	public PlainResult<String> uploadFile(FileItem fileItem, String destFileName) {
		PlainResult<String> result = new PlainResult<String>();

		//获取项目根路径
		String rootPath = PathUtil.getRootPath();
		// 按天进行分类
		String relativePath= defaultBaseDir+new SimpleDateFormat("yyyyMMdd").format(new Date()) + File.separatorChar;
		// 判断文件是否存在，不存在创建文件
		File tempFile = new File(rootPath+File.separatorChar+relativePath);
		if (!tempFile.exists()) {
			tempFile.mkdir();
		}
		File destFile = new File(tempFile.getPath(), destFileName);
		if (destFile.exists()) {
			result.setError(CommonResultCode.BIZ_ERROR, "文件已存在，上传失败");
			return result;
		}

		try {
			FileUtils.copyInputStreamToFile(fileItem.getInputStream(), destFile);
		} catch (IOException e) {
			 log.error("error message", e);
	         result.setError(CommonResultCode.BIZ_ERROR, "文件上传失败");
		}

		result.setData(relativePath+destFileName);
		return result;
	}
	
	
}
