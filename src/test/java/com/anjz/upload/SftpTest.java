package com.anjz.upload;

import java.io.FileInputStream;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anjz.util.PropertiesUtil;
import com.anjz.util.sftp.SFTPChannel;
import com.anjz.util.sftp.SftpUtil;
import com.jcraft.jsch.ChannelSftp;

/**
 * sftp 测试
 * 
 * @author shuai.ding
 * @date 2017年3月4日上午11:27:23
 */
public class SftpTest{
	public static final Logger logger = LoggerFactory.getLogger(SftpTest.class);
	

	public SFTPChannel sftpChannel = new SFTPChannel();
	public ChannelSftp channelSftp;
	
	@Before
	public void getChannel() {
		PropertiesUtil propertiesUtil = PropertiesUtil.getInstance("config.properties");
		String host = propertiesUtil.getValue("sftp.host");
		String port = propertiesUtil.getValue("sftp.port");
		String username = propertiesUtil.getValue("sftp.username");
		String password = propertiesUtil.getValue("sftp.password");

	    channelSftp = sftpChannel.getChannel(host, Integer.valueOf(port), username, password);
	}
	
	/**
	 * 判断目录是否存在
	 */
	@Test
	public void isDirExistTest(){
		logger.info(SftpUtil.isDirExist(channelSftp, "/usr/local/nginx-1.7.8/a/html")+"");
	}
	
	/**
	 * 创建目录
	 */
	@Test
	public void createDir(){
		SftpUtil.createDir("/usr/local/nginx-1.7.8/html/frame/20170304", channelSftp);
	}
	
	/**
	 * 上传
	 * @throws Exception 
	 */
	@Test
	public void uploadTest() throws Exception {
//		SftpUtil.upload("C:/Users/Administrator/Desktop/markers.png","/usr/local/nginx-1.7.8/html/frame/20170304/a.png", channelSftp);
				
		SftpUtil.upload(new FileInputStream("C:/Users/Administrator/Desktop/新建文本文档.txt"), "/usr/local/nginx-1.7.8/html/frame/20170304/d/aa.txt", channelSftp);
		
//		SftpUtil.uploadByStream("C:/Users/Administrator/Desktop/markers.png", "/usr/local/nginx-1.7.8/html/frame/20170304/ccc.png", channelSftp);
	}
	
	
	
	public void closeChannel(){
		sftpChannel.closeChannel();
	}
}
