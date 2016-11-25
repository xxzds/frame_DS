package com.anjz.upload;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.anjz.result.PlainResult;

/**
 * 上传到文件服务器
 * @author ding.shuai
 * @date 2016年8月17日上午9:09:53
 */
public class FileUploadToServerServiceImpl implements FileUploadService {

	@Override
	public PlainResult<String> uploadFile(MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlainResult<String> uploadFile(MultipartFile file, String[] allowedExtension) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlainResult<String> uploadFile(MultipartFile file, String[] allowedExtension, long maxSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlainResult<String> uploadFile(MultipartFile file, String[] allowedExtension, long maxSize,
			boolean needDatePathAndRandomName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PlainResult<String> uploadFile(MultipartFile file, String baseDir, String[] allowedExtension, long maxSize,
			boolean needDatePathAndRandomName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String filename) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
}
